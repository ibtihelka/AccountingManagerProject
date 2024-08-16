package com.accounting_manager.accounting_engine.Service;

import com.accounting_manager.AccountingCore.XsltTransformer;
import com.accounting_manager.accounting_engine.Classes.facture.Facture;
import com.accounting_manager.accounting_engine.Classes.facture.FactureInfo;
import com.accounting_manager.accounting_engine.Classes.facture.MultiFacture;
import com.accounting_manager.accounting_engine.Classes.facture.ProcessingOption;
import com.accounting_manager.accounting_engine.Entity.Invoice;
import com.accounting_manager.accounting_engine.Entity.Type;
import com.accounting_manager.accounting_engine.Repository.InvoiceRepository;
import com.accounting_manager.accounting_engine.Repository.TypeRepository;
import com.accounting_manager.accounting_engine.XMLReader.Dom4jXmlReader;
import com.asprise.ocr.Ocr;
import com.accounting_manager.accounting_engine.Classes._Out._Out;
import com.accounting_manager.accounting_engine.Classes.document._Document;
import com.accounting_manager.accounting_engine.Classes.learning.LearningEngine;
import com.accounting_manager.accounting_engine.Ocr.com.abbyy.orcsdk.Client;
import com.accounting_manager.accounting_engine.Ocr.com.abbyy.orcsdk.ProcessingSettings;
import com.accounting_manager.accounting_engine.Ocr.com.abbyy.orcsdk.Task;
import lombok.extern.log4j.Log4j2;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * EngineService is a service class that handles the processing of invoices using different OCR engines.
 * It uses the Abbyy, Tesseract, and Asprise OCR engines to process invoices concurrently.
 * It also handles the configuration of these OCR engines.
 */
@Service
@Log4j2
public class EngineService {
    /**
     * The Abbyy OCR engine client.
     */
    private static final Client restClient = new Client();
    /**
     * The path to the resources' directory.
     */
    private static String resourcesPath;

    private static String xmlFiles;
    /**
     * The Dom4j XML reader.
     */
    private final Dom4jXmlReader dom4jHandler = new Dom4jXmlReader();
    /**
     * The invoice repository.
     */
    private InvoiceRepository invoiceRepository;
    /**
     * The type repository.
     */
    private TypeRepository typeRepository;

    /**
     * The EngineService constructor.
     * @param invoiceRepository The invoice repository.
     * @param typeRepository The type repository.
     * @throws URISyntaxException If the URI is invalid.
     */
    public EngineService(InvoiceRepository invoiceRepository,TypeRepository typeRepository) throws URISyntaxException {
        if (Files.exists(Path.of("AccountingEngine/src/main/resources/"))) {
            resourcesPath="AccountingEngine/src/main/resources/";
            xmlFiles=resourcesPath+"/tmp/";
        }
        else {
            resourcesPath="src/main/resources/";
            xmlFiles="/tmp/accounting_engine/";
        }
        this.invoiceRepository = invoiceRepository;
        this.typeRepository = typeRepository;
    }

    /**
     * This method processes invoices concurrently using the Abbyy OCR engine.
     * It applies a threshold filter to the invoice image, processes the image using the Abbyy OCR engine,
     * reads the output XML file, and saves the processed invoice to the database.
     * @param invoiceId The ID of the invoice to be processed.
     * @return A CompletableFuture of the processed Invoice.
     */
    @Async
    public CompletableFuture<Invoice> processInvoicesConcurrentlyUsingAbbyy(Long invoiceId) {
        CompletableFuture<Invoice> future = new CompletableFuture<>();
        ProcessingSettings settings = new ProcessingSettings();
        settings.setLanguage("French,English");
        settings.setTextType("normal,matrix,typewriter");
        settings.setProfile("documentConversion");
        settings.setOutputFormat(ProcessingSettings.OutputFormat.xmlForCorrectedImage);
        // Process each invoice concurrently here.
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        // Process the invoice using your accountant engine logic.
        if (invoiceOptional.isPresent()) {
            // accountant engine logic goes here.
            byte[] imageprocess = invoiceOptional.get().getSourceImg();
            URL url = null;
            try {
                Task task = restClient.processByteArrayImage(imageprocess, settings);
                while (task.isTaskActive()) {
                    Thread.sleep(1000);
                    log.info("En attente..");
                    task = restClient.getTaskStatus(task.Id);
                    log.info("Task ID : " + task.Id);
                }
                // Si la tâche OCR est terminée, télécharger le fichier XML de sortie
                if (task.Status == Task.TaskStatus.Completed) {
                    log.info("Getting Xml URL....");
                    url = restClient.getURL(task);
                } else {
                    log.error("La tâche a échoué");
                }

                // Créer un objet Dom4jHandler et lire le fichier XML de sortie avec lui.
                List<_Document> docs = dom4jHandler.readXmlFromUrl(url);
                _Document s = docs.get(0);
                _Out<_Document[]> allDocuments = new _Out<>(docs.toArray(new _Document[docs.size()]));
                imageprocess = s.ByteArrayImage("png");
                invoiceOptional.get().setDetectionImg(imageprocess);
                invoiceRepository.save(invoiceOptional.get());
                MultiFacture multiFacture = new MultiFacture("output.xml", resourcesPath, "01021000", false, "sa", new LearningEngine());
                Facture facture = new Facture(multiFacture, 0, "output.xml");
                FactureInfo facture1 = facture.startProcessing(ProcessingOption.NONE, allDocuments);

                Invoice inv = invoiceOptional.get().copyFromInvoiceInfo(facture1);
                inv=setProcessType(inv);
                future.complete(inv);
                return future;
            } catch (Exception e) {
                log.error("An error occurred while processing the image: " + e);
                future.complete(null);
            }
        }

        return future;
    }

    /**
     * This method configures the Tesseract OCR engine and processes invoices concurrently.
     * It sets the data path, language, page segmentation mode, and other settings for the Tesseract OCR engine,
     * applies a threshold filter to the invoice image, and processes the image using the Tesseract OCR engine.
     * @param invoiceId The ID of the invoice to be processed.
     * @return A CompletableFuture of the processed Invoice.
     */
    @Async
    public CompletableFuture<Invoice> configureTesseract(Long invoiceId) throws FileNotFoundException, URISyntaxException {

        CompletableFuture<Invoice> future = new CompletableFuture<>();
        ITesseract tesseract = new Tesseract();

        var libPath ="";
        if (Files.exists(Path.of(resourcesPath + "lib/tessdata"))) {
            //local
            libPath=resourcesPath+"lib/tessdata";
        }
        else {
            //docker
            libPath="/usr/share/tesseract-ocr/4.00/tessdata";
        }
        tesseract.setDatapath(libPath);
        tesseract.setLanguage("fra");
        tesseract.setPageSegMode(2);
        tesseract.setTessVariable("tessedit_create_alto","1");

        // Process each invoice concurrently here.
        if (invoiceRepository.findById(invoiceId).isPresent()) {

            Invoice invoice = invoiceRepository.findById(invoiceId).get();

            // accountant engine logic goes here.
            byte[] imageProcess = invoice.getSourceImg();
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageProcess));

                String scanResult = tesseract.doOCR(image);
                future = processInvoicesConcurrentlyUsingOtherOCR("tesseract",future,invoice,scanResult);
            } catch (Exception e) {
                log.error("An error occurred while processing the image: " + e);
            }
        }
        return future;
    }

    /**
     * This method configures the Asprise OCR engine and processes invoices concurrently.
     * It sets up the Asprise OCR engine, applies a threshold filter to the invoice image,
     * and processes the image using the Asprise OCR engine.
     * @param invoiceId The ID of the invoice to be processed.
     * @return A CompletableFuture of the processed Invoice.
     */
    @Async
    public CompletableFuture<Invoice> configureAsprise(Long invoiceId) {

        CompletableFuture<Invoice> future = new CompletableFuture<>();
        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new ASPRISE OCR engine
        ocr.startEngine("fra", Ocr.SPEED_FASTEST); // English

        // Process each invoice concurrently here.
        if (invoiceRepository.findById(invoiceId).isPresent()) {

            Invoice invoice = invoiceRepository.findById(invoiceId).get();

            // accountant engine logic goes here.
            byte[] imageProcess = invoice.getSourceImg();
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageProcess));

                String scanResult = ocr.recognize(image, Ocr.RECOGNIZE_TYPE_ALL,
                        Ocr.OUTPUT_FORMAT_XML);
                ocr.stopEngine();
                future = processInvoicesConcurrentlyUsingOtherOCR("asprise",future,invoice,scanResult);
            } catch (Exception e) {
                log.error("An error occurred while processing the image: " + e);
            }
        }
        return future;
    }

    /**
     * This method processes invoices concurrently using other OCR engines.
     * It transforms the output XML file, reads the transformed XML file, saves the processed invoice to the database,
     * and deletes the source and transformed XML files.
     * @param selectedOcr The selected OCR engine.
     * @param future A CompletableFuture of the processed Invoice.
     * @param invoice The invoice to be processed.
     * @param scanResult The result of the OCR scan.
     * @return A CompletableFuture of the processed Invoice.
     */
    public CompletableFuture<Invoice> processInvoicesConcurrentlyUsingOtherOCR(String selectedOcr, CompletableFuture<Invoice> future, Invoice invoice, String scanResult) throws Exception {

        String sourceXmlFilePath = xmlFiles+ "/source_xml/" + invoice.getId() + ".xml";
        String transformedXsltFilePath = xmlFiles+ "/transformed_xml/" + invoice.getId() + ".xml";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputSource inputSource = new InputSource(new StringReader(scanResult));
        Document document = builder.parse(inputSource);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        // Writing to XML file
        try (FileWriter writer = new FileWriter(sourceXmlFilePath)) {
            StreamResult streamResult = new StreamResult(writer);
            transformer.transform(domSource, streamResult);

            XsltTransformer xsltTransformer = new XsltTransformer(selectedOcr);
            try {
                String transformedXml = xsltTransformer.transform(sourceXmlFilePath);
                xsltTransformer.saveTransformedXml(transformedXml, transformedXsltFilePath);
            } catch (Exception e) {
                log.error("Error transforming xml file");
            }
        }

        // Créer un objet Dom4jHandler et lire le fichier XML de sortie avec lui.
        List<_Document> docs = dom4jHandler.readXml(transformedXsltFilePath);
        _Document s = docs.get(0);
        _Out<_Document[]> allDocuments = new _Out<>(docs.toArray(new _Document[docs.size()]));
        byte[] imageProcess = s.ByteArrayImage("png");

        invoice.setDetectionImg(imageProcess);
        invoiceRepository.save(invoice);
        MultiFacture multiFacture = new MultiFacture(transformedXsltFilePath, resourcesPath, "01021000", false, "sa", new LearningEngine());
        Facture facture = new Facture(multiFacture, 0, transformedXsltFilePath);
        FactureInfo facture1 = facture.startProcessing(ProcessingOption.NONE, allDocuments);
        Invoice inv = invoice.copyFromInvoiceInfo(facture1);
        inv=setProcessType(inv);
        future.complete(inv);
        Files.deleteIfExists(Paths.get(transformedXsltFilePath));
        Files.deleteIfExists(Paths.get(sourceXmlFilePath));
        return future;
    }

    /**
     * This method sets the process type of the invoice.
     * If the invoice has a number, date, TVA number, SIRET number, supplier number, number of pages, or TTC,
     * it sets the process type to AUTO.
     * @param inv The invoice to set the process type for.
     * @return The invoice with the set process type.
     */
    private Invoice setProcessType(Invoice inv) {
        if ((inv.getInvoiceNumber() != null || inv.getInvoiceDate() != null
                || inv.getTvaNumber() != null || inv.getSiretNumber() != null
                || inv.getSupplierNumber() != null && inv.getPagesNumber() != 0
                || inv.getTtc() != 0)) {
            Type autoProcessedInvoiceType = typeRepository.findOneByParentCodeTypeAndCodeType("DOCUMENT_PROCESS", "AUTO");
            inv.setProcessType(autoProcessedInvoiceType);
        }
        return inv;
    }

}