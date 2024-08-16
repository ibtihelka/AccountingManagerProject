package com.accounting_manager.accounting_management.Service;

import com.accounting_manager.accounting_management.Entity.BankStatementNoSql;
import com.accounting_manager.accounting_management.Entity.DocumentNoSql;
import com.accounting_manager.accounting_management.Entity.FolderNoSql;
import com.accounting_manager.accounting_management.Entity.InvoiceNoSql;
import com.accounting_manager.accounting_management.Repository.*;
import jakarta.ws.rs.BadRequestException;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@Service
@Profile("nosql")
public class DocumentServiceNoSql {

    private final DocumentRepositoryNoSql documentRepositoryNoSql;
    private final InvoiceRepositoryNoSql invoiceRepositoryNoSql;
    private final BankStatementRepositoryNoSql bankStatementRepositoryNoSql;
    private final TypeRepositoryNoSql typeRepositoryNoSql;
    private final FolderRepositoryNoSql folderRepositoryNoSql;

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecret;

    public DocumentServiceNoSql(
            DocumentRepositoryNoSql documentRepositoryNoSql,
            InvoiceRepositoryNoSql invoiceRepositoryNoSql,
            BankStatementRepositoryNoSql bankStatementRepositoryNoSql,
            TypeRepositoryNoSql typeRepositoryNoSql,
            FolderRepositoryNoSql folderRepositoryNoSql) {
        this.documentRepositoryNoSql = documentRepositoryNoSql;
        this.invoiceRepositoryNoSql = invoiceRepositoryNoSql;
        this.bankStatementRepositoryNoSql = bankStatementRepositoryNoSql;
        this.typeRepositoryNoSql = typeRepositoryNoSql;
        this.folderRepositoryNoSql = folderRepositoryNoSql;
    }

    public Page<DocumentNoSql> getDocuments(String token, String folderId, String query, int page, int size) {
        if (token != null && Jwts.parser().setSigningKey(jwtSecret).isSigned(token.substring(7))) {
            Object thirdPartyObject = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token.substring(7))
                    .getBody()
                    .get("thirdParty");

            Map<String, Object> thirdPartyMap = (Map<String, Object>) thirdPartyObject;
            String thirdPartyId = thirdPartyMap.get("idThirdParty").toString();

            // Récupérer le dossier par son ID
            Optional<FolderNoSql> folderOpt = folderRepositoryNoSql.findById(folderId);
            if (folderOpt.isPresent()) {
                FolderNoSql folder = folderOpt.get();

                // Vérifier si l'utilisateur est le client ou le créateur du dossier
                if (folder.getClient().getIdThirdParty().equals(thirdPartyId) || folder.getCreator().getIdThirdParty().equals(thirdPartyId)) {
                    if (size <= 0) {
                        size = 5;
                    }
                    return documentRepositoryNoSql.findAll(folderId, query, PageRequest.of(page, size));
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ACCESS DENIED");
    }

    public Page<DocumentNoSql> getDocumentsForAdmin(String folder, String query, String type, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        Pageable pageable = PageRequest.of(page, size);
        return documentRepositoryNoSql.findAllForAdmin(folder, query, pageable);
    }

    public Optional<DocumentNoSql> getDocumentById(String id) {
        return documentRepositoryNoSql.findById(id);
    }

    public Map<String, Object> createDocuments(String folderId, MultipartFile[] files, String folderType) {
        int uploadedDocumentsNumber = files.length;
        int savedDocumentsNumber = 0;
        List<Map<String, Object>> successfulDocuments = new ArrayList<>();
        List<Map<String, Object>> unsuccessfulDocuments = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                BufferedImage image;
                int index = file.getOriginalFilename().lastIndexOf('.');
                String extension = file.getOriginalFilename().substring(index + 1);
                if (extension.equals("pdf")) {
                    PDDocument document = PDDocument.load(file.getInputStream());
                    PDFRenderer renderer = new PDFRenderer(document);
                    image = renderer.renderImage(0);
                    document.close();
                } else {
                    image = ImageIO.read(file.getInputStream());
                }
                ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "png", pngOutputStream);
                byte[] pngBytes = pngOutputStream.toByteArray();

                String fileNameWithoutExtension = file.getOriginalFilename().substring(0, index);
                Optional<InvoiceNoSql> createdInvoice = Optional.empty();
                Optional<BankStatementNoSql> createdBankStatement = Optional.empty();

                switch (folderType) {
                    case "INVOICES":
                        var invoiceDocument = InvoiceNoSql.builder()
                                .sourceImg(pngBytes)
                                .name(fileNameWithoutExtension)
                                .folder(FolderNoSql.builder().idFolder(folderId).build())
                                .build();
                        if (documentRepositoryNoSql.documentIsPresent(pngBytes, folderId)) {
                            InvoiceNoSql existingInvoice = invoiceRepositoryNoSql.getDocumentBySourceImg(pngBytes, folderId);
                            if (existingInvoice.isDeleted()) {
                                invoiceDocument.setId(existingInvoice.getId());
                                invoiceDocument.setCreationDate(LocalDateTime.now());
                                createdInvoice = Optional.of(invoiceRepositoryNoSql.save(invoiceDocument));
                            } else
                                throw new DuplicateKeyException("INVOICE ALREADY EXISTS IN THIS FOLDER");
                        }
                        createdInvoice = Optional.of(invoiceRepositoryNoSql.save(invoiceDocument));
                        break;
                    case "BANK_STATEMENTS":
                        var bankStatementDocument = BankStatementNoSql.builder()
                                .sourceImg(pngBytes)
                                .name(fileNameWithoutExtension)
                                .folder(FolderNoSql.builder().idFolder(folderId).build())
                                .build();
                        if (documentRepositoryNoSql.documentIsPresent(pngBytes, folderId)) {
                            BankStatementNoSql existingBankStatement = bankStatementRepositoryNoSql.getDocumentBySourceImg(pngBytes, folderId);
                            if (existingBankStatement.isDeleted()) {
                                bankStatementDocument.setId(existingBankStatement.getId());
                                bankStatementDocument.setCreationDate(LocalDateTime.now());
                                createdBankStatement = Optional.of(bankStatementRepositoryNoSql.save(bankStatementDocument));
                            } else
                                throw new DuplicateKeyException("INVOICE ALREADY EXISTS IN THIS FOLDER");
                        }
                        createdBankStatement = Optional.of(bankStatementRepositoryNoSql.save(bankStatementDocument));
                        break;
                    default:
                        throw new BadRequestException("INVALID FOLDER TYPE");
                }

                if (createdBankStatement.isPresent() || createdInvoice.isPresent()) {
                    savedDocumentsNumber++;
                    Map<String, Object> documentDetails = new HashMap<>();
                    documentDetails.put("id", createdInvoice.isPresent() ? createdInvoice.get().getId() : createdBankStatement.get().getId());
                    documentDetails.put("filename", file.getOriginalFilename());

                    successfulDocuments.add(documentDetails);
                }
            } catch (Exception e) {
                log.error("AN ERROR HAS BEEN OCCURRED WHILE UPLOADING THIS DOCUMENT : " + file.getName() + " ->\n" + e.getMessage());
                Map<String, Object> documentDetails = new HashMap<>();
                documentDetails.put("filename", file.getOriginalFilename());
                documentDetails.put("reason", e.getMessage());
                unsuccessfulDocuments.add(documentDetails);
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("totalDocuments", uploadedDocumentsNumber);
        response.put("successfullySaved", savedDocumentsNumber);
        response.put("successfulDocuments", successfulDocuments);
        response.put("unsuccessfulDocuments", unsuccessfulDocuments);
        return response;
    }

    public Optional<DocumentNoSql> updateInvoice(String id, InvoiceNoSql updatedInvoice) {
        if (documentRepositoryNoSql.existsById(id)) {
            if (updatedInvoice.getName() != null && updatedInvoice.getInvoiceNumber() != null
                    && updatedInvoice.getTvaNumber() != null && updatedInvoice.getSiretNumber() != null
                    && updatedInvoice.getSupplierNumber() != null
                    && updatedInvoice.getTva() != 0 && updatedInvoice.getHt() != 0) {
                updatedInvoice.setStatus(true);
            }
            double ttc = (updatedInvoice.getHt() + updatedInvoice.getTva() - updatedInvoice.getDiscount()) + updatedInvoice.getSupportTva();
            updatedInvoice.setTtc(ttc);

            if (updatedInvoice.getProcessType() == null) {
                updatedInvoice.setProcessType(typeRepositoryNoSql.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "MANUAL"));
            } else if (updatedInvoice.getProcessType().getCodeType().equals("AUTO")) {
                updatedInvoice.setProcessType(typeRepositoryNoSql.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "AUTO"));
            } else if (updatedInvoice.getProcessType().getCodeType().equals("AUTO_MANUAL")) {
                updatedInvoice.setProcessType(typeRepositoryNoSql.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "AUTO_MANUAL"));
            }

            updatedInvoice.setId(id);
            return Optional.of(invoiceRepositoryNoSql.save(updatedInvoice));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "INVOICE NOT FOUND");
    }

    public Optional<DocumentNoSql> updateBankStatement(String id, BankStatementNoSql updatedBankStatement) {
        if (documentRepositoryNoSql.existsById(id)) {
            if (updatedBankStatement.getName() != null && updatedBankStatement.getAccountNumber() != null
                    && updatedBankStatement.getBic() != null && updatedBankStatement.getRib() != null
                    && updatedBankStatement.getIban() != null) {
                updatedBankStatement.setStatus(true);
            }
            if (updatedBankStatement.getProcessType() == null) {
                updatedBankStatement.setProcessType(typeRepositoryNoSql.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "MANUAL"));
            } else if (updatedBankStatement.getProcessType().getCodeType().equals("AUTO")) {
                updatedBankStatement.setProcessType(typeRepositoryNoSql.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "AUTO"));
            } else if (updatedBankStatement.getProcessType().getCodeType().equals("AUTO_MANUAL")) {
                updatedBankStatement.setProcessType(typeRepositoryNoSql.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "AUTO_MANUAL"));
            }
            updatedBankStatement.setId(id);
            return Optional.of(bankStatementRepositoryNoSql.save(updatedBankStatement));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BANK STATEMENT NOT FOUND");
    }

    public void markDocumentAsDeleted(String id) {
        if (documentRepositoryNoSql.existsById(id)) {
            documentRepositoryNoSql.markDocumentAsDeleted(id);
        }
    }

    public void deleteDocumentPermanently(String id) {
        if (documentRepositoryNoSql.existsById(id)) {
            documentRepositoryNoSql.deleteById(id);
        }
    }

    public Long getTotalDocuments() {
        return this.documentRepositoryNoSql.count();
    }

    public Long getTotalDocumentsByThirdPartyId(String id) {
        return this.documentRepositoryNoSql.getTotalDocumentsByThirdPartyId(id);
    }


    public Double autoProcessEfficiency() {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);

        double percentage = ((double) this.documentRepositoryNoSql.findAutoProcessedDocuments() / this.getTotalDocuments()) * 100;
        String formattedPercentage = decimalFormat.format(percentage);

        return Double.parseDouble(formattedPercentage);
    }

/*
    public Map<String, Integer> getProcessTypeCount() {
        List<Object[]> results = this.documentRepositoryNoSql.findProcessTypeCount();
        Map<String, Integer> processTypeCountMap = new HashMap<>();

        int sum = 0;
        for (Object[] result : results) {
            String processType = result[0].toString();
            Long count = (Long) result[1];
            processTypeCountMap.put(processType, count.intValue());
            sum += count.intValue();
        }
        processTypeCountMap.put("UNPROCESSED", this.getTotalDocuments().intValue() - sum);

        return processTypeCountMap;
    }

    public List<Long> getDocumentsCountByMonthCurrentYear() {
        Long[] array = new Long[12];
        Arrays.fill(array, 0L);

        for (Object[] result : documentRepositoryNoSql.findDocumentsCountByMonthCurrentYear()) {
            int month = (int) result[0];
            Long count = (Long) result[1];

            array[month - 1] = count;
        }
        return Arrays.asList(array);
    }

*/

}
