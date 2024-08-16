package com.accounting_manager.accounting_management.Service;

import com.accounting_manager.accounting_management.Entity.BankStatement;
import com.accounting_manager.accounting_management.Entity.Document;
import com.accounting_manager.accounting_management.Entity.Folder;
import com.accounting_manager.accounting_management.Entity.Invoice;
import com.accounting_manager.accounting_management.Repository.*;
import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import static java.lang.Long.parseLong;

@Log4j2
@Service
@Profile("sql")
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final InvoiceRepository invoiceRepository;
    private final BankStatementRepository bankStatementRepository;
    private final TypeRepository typeRepository;
    private final FolderRepository folderRepository;

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecret;

    public DocumentService(
            DocumentRepository documentRepository,
            InvoiceRepository invoiceRepository, BankStatementRepository bankStatementRepository, TypeRepository typeRepository,
            FolderRepository folderRepository) {
        this.documentRepository = documentRepository;
        this.invoiceRepository = invoiceRepository;
        this.bankStatementRepository = bankStatementRepository;
        this.typeRepository = typeRepository;
        this.folderRepository=folderRepository;
    }

    public Page<Document> getDocuments(String token, Long folder, String query, int page, int size) {
        if (token != null && Jwts.parser().setSigningKey(jwtSecret).isSigned(token.substring(7))){
            Object thirdPartyObject = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token.substring(7))
                    .getBody()
                    .get("thirdParty");

            Map<String, Object> thirdPartyMap = (Map<String, Object>) thirdPartyObject;
            Long id = parseLong(thirdPartyMap.get("idThirdParty").toString());
            if(folderRepository.findById(folder).get().getClient().getIdThirdParty().equals(id) || folderRepository.findById(folder).get().getCreator().getIdThirdParty().equals(id)) {
                if (size <= 0) {
                    size = 5;
                }
                return documentRepository.findAll(folder, query, PageRequest.of(page, size));
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    public Page<Document> getDocumentsForAdmin(Long folder, String query, String type, int page, int size) {
        if (size <= 0) {
            size = 5;
        }
        return documentRepository.findAllForAdmin(folder, query, type, PageRequest.of(page, size));
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public Map<String, Object> createDocuments(Long folderId, MultipartFile[] files, String folderType){
        int uploadedDocumentsNumber = files.length;
        int savedDocumentsNumber = 0;
        List<Map<String, Object>> successfulDocuments = new ArrayList<>();
        List<Map<String, Object>> unsuccessfulDocuments = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                BufferedImage image;
                // Extracting file name
                int index = file.getOriginalFilename().lastIndexOf('.');
                // Extracting file extension
                String extension = file.getOriginalFilename().substring(index + 1);
                // Rendering image from pdf if extension is pdf
                if (extension.equals("pdf")) {
                    PDDocument document = PDDocument.load(file.getInputStream());
                    // Instantiation de la classe PDFRenderer
                    PDFRenderer renderer = new PDFRenderer(document);
                    // Rendering an image from the PDF document
                    image = renderer.renderImage(0);
                    // Closing PDF document
                    document.close();
                } else {
                    // Read image using ImageIO
                    image = ImageIO.read(file.getInputStream());
                }
                // Convert to PNG format
                ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "png", pngOutputStream);

                // Return the PNG image as a byte array
                byte[] pngBytes = pngOutputStream.toByteArray();

                String fileNameWithoutExtension = file.getOriginalFilename().substring(0,index);
                Optional<Invoice> createdInvoice = Optional.empty();
                Optional<BankStatement> createdBankStatement = Optional.empty();

                switch (folderType){
                    case "INVOICES":
                        var invoiceDocument = Invoice.builder()
                                .sourceImg(pngBytes)
                                .name(fileNameWithoutExtension)
                                .folder(Folder.builder().idFolder(folderId).build())
                                .build();
                        if (documentRepository.documentIsPresent(pngBytes, folderId)) {
                            Invoice existingInvoice = invoiceRepository.getDocumentBySourceImg(pngBytes, folderId);
                            if (existingInvoice.isDeleted()) {
                                invoiceDocument.setId(existingInvoice.getId());
                                invoiceDocument.setCreationDate(LocalDateTime.now());
                                createdInvoice = Optional.of(invoiceRepository.save(invoiceDocument));
                            } else
                                throw new DuplicateKeyException("INVOICE ALREADY EXISTS IN THIS FOLDER");
                        }
                        createdInvoice = Optional.of(invoiceRepository.save(invoiceDocument));
                        break;
                    case "BANK_STATEMENTS":
                        var bankStatementDocument = BankStatement.builder()
                                .sourceImg(pngBytes)
                                .name(fileNameWithoutExtension)
                                .folder(Folder.builder().idFolder(folderId).build())
                                .build();
                        if (documentRepository.documentIsPresent(pngBytes, folderId)) {
                            BankStatement existingBankStatement = bankStatementRepository.getDocumentBySourceImg(pngBytes, folderId);
                            if (existingBankStatement.isDeleted()) {
                                bankStatementDocument.setId(existingBankStatement.getId());
                                bankStatementDocument.setCreationDate(LocalDateTime.now());
                                createdBankStatement = Optional.of(bankStatementRepository.save(bankStatementDocument));
                            } else
                                throw new DuplicateKeyException("INVOICE ALREADY EXISTS IN THIS FOLDER");
                        }
                        createdBankStatement = Optional.of(bankStatementRepository.save(bankStatementDocument));
                        break;
                    default:
                        throw new BadRequestException("INVALID FOLDER TYPE");
                }

                if(createdBankStatement.isPresent() || createdInvoice.isPresent()){

                    savedDocumentsNumber++;
                    Map<String, Object> documentDetails = new HashMap<>();
                    documentDetails.put("id", createdInvoice.get().getId()!=null?createdInvoice.get().getId():createdBankStatement.get().getId());
                    documentDetails.put("filename", file.getOriginalFilename());

                    successfulDocuments.add(documentDetails);
                }
            } catch (Exception e) {
                log.error("AN ERROR HAS BEEN OCCURRED WHILE UPLOADING THIS INVOICE : "+file.getName()+" ->\n"+e.getMessage());
                Map<String, Object> documentDetails = new HashMap<>();
                documentDetails.put("filename", file.getOriginalFilename());
                documentDetails.put("reason", e.getMessage()); // Include the specific reason for the error
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

    public Optional<Document> updateInvoice(Long id, Invoice updatedInvoice) {
        if (documentRepository.existsById(id)) {
            if (updatedInvoice.getName() != null && updatedInvoice.getInvoiceNumber() != null
                    && updatedInvoice.getTvaNumber() != null && updatedInvoice.getSiretNumber() != null
                    && updatedInvoice.getSupplierNumber() != null
                    && updatedInvoice.getTva() != 0 && updatedInvoice.getHt() != 0) {
                updatedInvoice.setStatus(true);
            }
            // Calculate ttc based on the provided values
            double ttc = (updatedInvoice.getHt() + updatedInvoice.getTva() - updatedInvoice.getDiscount()) + updatedInvoice.getSupportTva();
            updatedInvoice.setTtc(ttc);

            if (updatedInvoice.getProcessType() == null) {
                updatedInvoice.setProcessType(typeRepository.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "MANUAL"));
            } else if (updatedInvoice.getProcessType().getCodeType().equals("AUTO")) {
                updatedInvoice.setProcessType(typeRepository.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "AUTO"));
            } else if (updatedInvoice.getProcessType().getCodeType().equals("AUTO_MANUAL")) {
                updatedInvoice.setProcessType(typeRepository.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "AUTO_MANUAL"));
            }

            updatedInvoice.setId(id);
            return Optional.of(documentRepository.save(updatedInvoice));
        }
        return Optional.empty();
    }

    public Optional<Document> updateBankStatement(Long id, BankStatement updatedBankStatement) {
        if (documentRepository.existsById(id)) {
            if (updatedBankStatement.getName() != null && updatedBankStatement.getAccountNumber() != null
                    && updatedBankStatement.getBic() != null && updatedBankStatement.getRib() != null
                    && updatedBankStatement.getIban() != null) {
                updatedBankStatement.setStatus(true);
            }

            if (updatedBankStatement.getProcessType() == null) {
                updatedBankStatement.setProcessType(typeRepository.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "MANUAL"));
            } else if (updatedBankStatement.getProcessType().getCodeType().equals("AUTO")) {
                updatedBankStatement.setProcessType(typeRepository.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "AUTO"));
            } else if (updatedBankStatement.getProcessType().getCodeType().equals("AUTO_MANUAL")) {
                updatedBankStatement.setProcessType(typeRepository.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "AUTO_MANUAL"));
            }

            updatedBankStatement.setId(id);
            return Optional.of(documentRepository.save(updatedBankStatement));
        }
        return Optional.empty();
    }

    public void markDocumentAsDeleted(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.markDocumentAsDeleted(id);
        }
    }

    public void deleteDocumentPermanently(Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
        }
    }

    public Long getTotalDocuments() {
        return this.documentRepository.count();
    }

    public Long getTotalDocumentsByThirdPartyId(Long id) {
        return this.documentRepository.getTotalDocumentsByThirdPartyId(id);
    }

    public Double autoProcessEfficiency() {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);

        double percentage = ((double) this.documentRepository.findAutoProcessedDocuments() / this.getTotalDocuments()) * 100;
        String formattedPercentage = decimalFormat.format(percentage);

        return Double.parseDouble(formattedPercentage);
    }

    public Map<String, Integer> getProcessTypeCount() {
        List<Object[]> results = this.documentRepository.findProcessTypeCount();
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

        for (Object[] result : documentRepository.findDocumentsCountByMonthCurrentYear()) {
            int month = (int) result[0];
            Long count = (Long) result[1];

            array[month - 1] = count;
        }
        return Arrays.asList(array);
    }

}
