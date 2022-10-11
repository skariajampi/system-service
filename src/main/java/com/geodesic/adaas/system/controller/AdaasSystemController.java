package com.geodesic.adaas.system.controller;

import com.geodesic.adaas.system.dto.CreateUpdateSystemRequest;
import com.geodesic.adaas.system.dto.SystemResponse;
import com.geodesic.adaas.system.exception.RecordNotFoundException;
import com.geodesic.adaas.system.service.AdaasSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/systems")
@Validated
public class AdaasSystemController {

    private final AdaasSystemService adaasSystemService;

    @Autowired
    public AdaasSystemController(final AdaasSystemService adaasSystemService) {
        this.adaasSystemService = adaasSystemService;
    }

    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('MyGreeterLambda')")
    public SystemResponse getAdaasSystem(
            @PathVariable("code") final String code) throws RecordNotFoundException {

        return adaasSystemService.getAdaasSystem(code);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('MyGreeterLambda')")
    public List<SystemResponse> getAdaasSystems() {

        return adaasSystemService.getAdaasSystems();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MyGreeterLambda')")
    public SystemResponse save(
            @Valid @RequestBody final CreateUpdateSystemRequest createUpdateSystemRequest) {

        return adaasSystemService.save(createUpdateSystemRequest);
    }


    @PutMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('MyGreeterLambda')")
    public SystemResponse update(@PathVariable String code,
            @Valid @RequestBody final CreateUpdateSystemRequest createUpdateSystemRequest) throws RecordNotFoundException {

        return adaasSystemService.update(code, createUpdateSystemRequest);
    }


    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('MyGreeterLambda')")
    public void deleteAdaasSystem(@PathVariable String code) throws RecordNotFoundException {
        adaasSystemService.deleteByCode(code);
    }

}
