package com.accounting_manager.accounting_engine.Controller;

import com.accounting_manager.accounting_engine.Entity.Invoice;
import com.accounting_manager.accounting_engine.Repository.InvoiceRepository;
import com.accounting_manager.accounting_engine.Repository.TypeRepository;
import com.accounting_manager.accounting_engine.Service.EngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents the controller for the engine API endpoints related to invoice processing.
 */
@RestController
@RequestMapping("/api/v1/invoice-engine")
@CrossOrigin("*")
@Log4j2
public class EngineController {

    private EngineService engineService;
    private InvoiceRepository invoiceRepository;
    private TypeRepository typeRepository;

    /**
     * Constructs a new EngineController with the specified dependencies.
     *
     * @param engineService     the engine service
     * @param invoiceRepository the invoice repository
     * @param typeRepository    the type repository
     */
    public EngineController(EngineService engineService, InvoiceRepository invoiceRepository, TypeRepository typeRepository) {
        this.engineService = engineService;
        this.typeRepository = typeRepository;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Processes the invoices based on the provided request body and OCR type.
     * If the OCR type is not provided, the default OCR type is used.
     *
     * @param requestBody the request body containing the invoice IDs
     * @param ocr         the OCR type (optional)
     * @return the response entity containing the processed invoices
     */
    @Operation(summary = "Process Invoices", description = "Processes the invoices based on the provided request body and OCR type. If the OCR type is not provided, the default OCR type is used.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoices processed successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Invoice.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request body containing the invoice IDs. Example: {\"documentsId\": [1, 2, 3, 4]}",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class),
            examples = @ExampleObject(value = "{\"documentsId\": [1, 2, 3, 4]}")
            )
    )
    @PostMapping({"/process/{ocr}", "/process"})
    public ResponseEntity<?> processInvoices(
            @org.springframework.web.bind.annotation.RequestBody Map<String, Set<Long>> requestBody,
            @Parameter(description = "The OCR type", schema = @Schema(allowableValues = {"abbyy", "tesseract", "asprise"})) @PathVariable(required = false) String ocr) {
        // Create a CompletableFuture object for each invoice ID.
        Set<Long> documentsId = requestBody.get("documentsId");
        List<CompletableFuture<Invoice>> futures = new ArrayList<>();
        List<Invoice> invoices = new ArrayList<>();

        try {
            for (Long documentId : documentsId) {
                if (invoiceRepository.findById(documentId).isPresent()) {
                    if (ocr == null || ocr.isEmpty()) {
                        ocr = this.typeRepository.findOneByParentCodeType("DEFAULT_OCR").getCodeType().toLowerCase();
                    }
                    switch (ocr) {
                        case "abbyy":
                            futures.add(engineService.processInvoicesConcurrentlyUsingAbbyy(documentId));
                            break;
                        case "tesseract":
                            futures.add(engineService.configureTesseract(documentId));
                            break;
                        case "asprise":
                            futures.add(engineService.configureAsprise(documentId));
                            break;
                    }
                }
            }

            // Wait for all the CompletableFuture objects to complete.
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allFutures.join();

            // Collect the results of the CompletableFuture objects into a list.
            for (CompletableFuture<Invoice> future : futures) {
                invoices.add(future.get());
            }

        } catch (Exception e) {
            log.error("Error :", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(invoices);
    }

}
