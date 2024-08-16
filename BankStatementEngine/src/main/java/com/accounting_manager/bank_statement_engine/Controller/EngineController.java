package com.accounting_manager.bank_statement_engine.Controller;

import com.accounting_manager.bank_statement_engine.Entity.BankStatement;
import com.accounting_manager.bank_statement_engine.Repository.BankStatementRepository;
import com.accounting_manager.bank_statement_engine.Repository.TypeRepository;
import com.accounting_manager.bank_statement_engine.Service.EngineService;
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
 * This class represents the controller for the engine API endpoints related to bank statement processing.
 */
@RestController
@RequestMapping("/api/v1/bank-statement-engine")
@Log4j2
public class EngineController {

    private EngineService engineService;
    private BankStatementRepository bankStatementRepository;
    private TypeRepository typeRepository;

    /**
     * Constructs a new BankStatementEngineController with the specified dependencies.
     *
     * @param engineService     the engine service
     * @param bankStatementRepository the bank statement repository
     * @param typeRepository    the type repository
     */
    public EngineController(EngineService engineService, BankStatementRepository bankStatementRepository, TypeRepository typeRepository) {
        this.engineService = engineService;
        this.typeRepository = typeRepository;
        this.bankStatementRepository = bankStatementRepository;
    }

    /**
     * Processes the bank statements based on the provided request body and OCR type.
     * If the OCR type is not provided, the default OCR type is used.
     *
     * @param requestBody the request body containing the bank statements IDs
     * @param ocr         the OCR type (optional)
     * @return the response entity containing the processed bank statements
     */
    @Operation(summary = "Process Bank Statements", description = "Processes the bank statements based on the provided request body and OCR type. If the OCR type is not provided, the default OCR type is used.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank statements processed successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BankStatement.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request body containing the bank statements IDs. Example: {\"documentsId\": [1, 2, 3, 4]}",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class),
            examples = @ExampleObject(value = "{\"documentsId\": [1, 2, 3, 4]}"))
    )
    @PostMapping({"/process/{ocr}", "/process"})
    public ResponseEntity<?> processInvoices(
            @org.springframework.web.bind.annotation.RequestBody Map<String, Set<Long>> requestBody,
            @Parameter(description = "The OCR type", schema = @Schema(allowableValues = {"abbyy", "tesseract", "asprise"})) @PathVariable(required = false) String ocr) {
        // Create a CompletableFuture object for each bank statement ID.
        Set<Long> documentsId = requestBody.get("documentsId");
        List<CompletableFuture<BankStatement>> futures = new ArrayList<>();
        List<BankStatement> bankStatements = new ArrayList<>();

        try {
            for (Long documentId : documentsId) {
                if (bankStatementRepository.findById(documentId).isPresent()) {
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
            for (CompletableFuture<BankStatement> future : futures) {
                bankStatements.add(future.get());
            }

        } catch (Exception e) {
            log.error("Error :", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(bankStatements);
    }

}
