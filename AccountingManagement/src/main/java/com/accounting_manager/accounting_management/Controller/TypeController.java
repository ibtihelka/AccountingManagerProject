package com.accounting_manager.accounting_management.Controller;

import com.accounting_manager.accounting_management.Entity.Type;
import com.accounting_manager.accounting_management.Service.TypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing types related to management operations.
 */
@RestController
@RequestMapping("/api/v1/management/types")
@CrossOrigin(origins = "*")
@Profile("sql")
public class TypeController {

    private TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    /**
     * Retrieves all types.
     *
     * @return ResponseEntity containing a list of all types
     */
    @Operation(summary = "Get All Types",
            description = "Retrieves a list of all types.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of types")
    })
    @GetMapping
    public ResponseEntity<List<Type>> getAllTypes() {
        List<Type> types = typeService.getAllTypes();
        return ResponseEntity.ok(types);
    }

    /**
     * Retrieves a type by ID.
     *
     * @param id ID of the type to retrieve
     * @return ResponseEntity containing the retrieved type
     */
    @Operation(summary = "Get Type by ID",
            description = "Retrieves a type by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved type"),
            @ApiResponse(responseCode = "404", description = "Type not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Type> getTypeById(@PathVariable Long id) {
        Optional<Type> typeOptional = typeService.getTypeById(id);

        if (typeOptional.isPresent()) {
            return ResponseEntity.ok(typeOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new type.
     *
     * @param type Type object to create
     * @return ResponseEntity containing the created type
     */
    @Operation(summary = "Create Type",
            description = "Creates a new type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Type created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<Type> createType(@RequestBody Type type) {
        Optional<Type> createdType = typeService.createType(type);

        if (createdType.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdType.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Updates a type by ID.
     *
     * @param id          ID of the type to update
     * @param updatedType Updated Type object
     * @return ResponseEntity containing the updated type
     */
    @Operation(summary = "Update Type",
            description = "Updates a type by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type updated successfully"),
            @ApiResponse(responseCode = "404", description = "Type not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Type> updateType(@PathVariable Long id, @RequestBody Type updatedType) {
        Optional<Type> updated = typeService.UpdateType(id, updatedType);

        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a type by ID.
     *
     * @param id ID of the type to delete
     * @return ResponseEntity indicating success
     */
    @Operation(summary = "Delete Type",
            description = "Deletes a type by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        typeService.deleteType(id);
        return ResponseEntity.ok().build();
    }

}
