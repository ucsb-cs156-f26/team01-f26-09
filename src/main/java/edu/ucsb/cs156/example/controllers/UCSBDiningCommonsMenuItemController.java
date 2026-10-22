package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.UCSBDiningCommonsMenuItem;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsMenuItemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** This is a REST controller for UCSBDiningCommonsMenuItem */
@Tag(name = "UCSBDiningCommonsMenuItem")
@RequestMapping("/api/UCSBDiningCommonsMenuItem")
@RestController
@Slf4j
public class UCSBDiningCommonsMenuItemController extends ApiController {

  @Autowired UCSBDiningCommonsMenuItemRepository ucsbDiningCommonsMenuItemRepository;

  /**
   * THis method returns a list of all UCSBDiningCommonsMenuItem.
   *
   * @return a list of all UCSBDiningCommonsMenuItem
   */
  @Operation(summary = "List all ucsb dining commons")
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/all")
  public Iterable<UCSBDiningCommonsMenuItem> allCommonss() {
    Iterable<UCSBDiningCommonsMenuItem> commons = ucsbDiningCommonsMenuItemRepository.findAll();
    return commons;
  }

  /**
   * This method returns a single diningcommonsmenuitem.
   *
   * @param code code of the diningcommonsmenuitem
   * @param diningCommonsCode diningCommonsCode of the diningcommonsmenuitem
   * @param name name of the diningcommonsmenuitem
   * @param station station of the diningcommonsmenuitem
   * @return a single diningcommonsmenuitem
   */
  @Operation(summary = "Get a single commons menu item")
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("")
  public UCSBDiningCommonsMenuItem getById(@Parameter(name = "code") @RequestParam String code) {
    UCSBDiningCommonsMenuItem commons =
        ucsbDiningCommonsMenuItemRepository
            .findById(code)
            .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItem.class, code));

    return commons;
  }

  /**
   * This method creates a new diningcommons. Accessible only to users with the role "ROLE_ADMIN".
   *
   * @param code code of the diningcommons
   * @param name name of the diningcommons
   * @param hasSackMeal whether or not the commons has sack meals
   * @param hasTakeOutMeal whether or not the commons has take out meals
   * @param hasDiningCam whether or not the commons has a dining cam
   * @param latitude latitude of the commons
   * @param longitude logitude of the commons
   * @return the save diningcommons
   */
  @Operation(summary = "Create a new commons")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/post")
  public UCSBDiningCommonsMenuItem postCommons(
      @Parameter(name = "code") @RequestParam String code,
      @Parameter(name = "name") @RequestParam String name,
      @Parameter(name = "diningCommonsCode") @RequestParam String diningCommonsCode,
      @Parameter(name = "station") @RequestParam String station) {

    UCSBDiningCommonsMenuItem commons = new UCSBDiningCommonsMenuItem();
    commons.setCode(code);
    commons.setName(name);
    commons.setDiningCommonsCode(diningCommonsCode);
    commons.setStation(station);

    UCSBDiningCommonsMenuItem savedCommons = ucsbDiningCommonsMenuItemRepository.save(commons);

    return savedCommons;
  }

  /**
   * Delete a diningcommons. Accessible only to users with the role "ROLE_ADMIN".
   *
   * @param code code of the commons
   * @return a message indiciating the commons was deleted
   */
  @Operation(summary = "Delete a UCSBDiningCommonsMenuItem")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("")
  public Object deleteCommons(@Parameter(name = "code") @RequestParam String code) {
    UCSBDiningCommonsMenuItem commons =
        ucsbDiningCommonsMenuItemRepository
            .findById(code)
            .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItem.class, code));

    ucsbDiningCommonsMenuItemRepository.delete(commons);
    return genericMessage("UCSBDiningCommonsMenuItem with id %s deleted".formatted(code));
  }

  /**
   * Update a single diningcommonsmenuitem. Accessible only to users with the role "ROLE_ADMIN".
   *
   * @param code code of the diningcommons
   * @param incoming the new commons contents
   * @return the updated commons object
   */
  @Operation(summary = "Update a single commons")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("")
  public UCSBDiningCommonsMenuItem updateCommons(
      @Parameter(name = "code") @RequestParam String code,
      @RequestBody @Valid UCSBDiningCommonsMenuItem incoming) {

    UCSBDiningCommonsMenuItem commons =
        ucsbDiningCommonsMenuItemRepository
            .findById(code)
            .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItem.class, code));

    commons.setName(incoming.getName());
    commons.setDiningCommonsCode(incoming.getDiningCommonsCode());
    commons.setStation(incoming.getStation());

    ucsbDiningCommonsMenuItemRepository.save(commons);

    return commons;
  }
}
