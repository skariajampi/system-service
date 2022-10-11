package com.geodesic.adaas.system.service;

import com.geodesic.adaas.system.dto.CreateUpdateSystemRequest;
import com.geodesic.adaas.system.dto.SystemResponse;
import com.geodesic.adaas.system.entity.SystemEntity;
import com.geodesic.adaas.system.exception.RecordNotFoundException;
import com.geodesic.adaas.system.repository.SystemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdaasSystemServiceImplTest {

    @Mock
    private SystemRepository systemRepository;

    @InjectMocks
    private AdaasSystemServiceImpl adaasSystemService;

    @Test
    void save_givenSaveSuccessful_thenUpdatedRecordReturned() {

        // Given
        String code = "AFN";
        CreateUpdateSystemRequest createUpdateSystemRequest = CreateUpdateSystemRequest
                .builder()
                .code(code)
                .description("desc")
                .name("name")
                .build();
        SystemEntity systemEntity = SystemEntity.builder().code(code).build();
        given(systemRepository.save(any(SystemEntity.class)))
                .willReturn(systemEntity);

        // When

        SystemResponse systemResponse =
                adaasSystemService.save(createUpdateSystemRequest);

        // Then
        verify(systemRepository).save(any(SystemEntity.class));
        assertThat(systemResponse.getCode()).isEqualTo(code);
    }

    @Test
    void getAdaasSystem_givenRecordExists_thenReturnData() throws RecordNotFoundException {

        // Given
        String code = "AFN";
        SystemEntity systemEntity = SystemEntity.builder().code(code).build();
        given(systemRepository.findByCode(code))
                .willReturn(Optional.of(systemEntity));

        // When
        SystemResponse systemResponse = adaasSystemService.getAdaasSystem(code);

        // Then
        verify(systemRepository).findByCode(code);
        assertThat(systemResponse.getCode()).isEqualTo(code);
    }

    @Test
    void getAdaasSystems_givenRecordsExist_thenReturnData() {

        // Given
        List<SystemEntity> adaasSystemList = new ArrayList<>();
        adaasSystemList.add(SystemEntity.builder().code("code3").build());
        adaasSystemList.add(SystemEntity.builder().code("code4").build());

        given(systemRepository.findAll()).willReturn(adaasSystemList);

        // When
        List<SystemResponse> systemResponseList = adaasSystemService.getAdaasSystems();

        // Then
        verify(systemRepository).findAll();
        assertThat(systemResponseList).hasSameSizeAs(adaasSystemList)
                .extracting("code").contains("code3", "code4");
    }

    @Test
    void getAdaasSystem_givenRecordDoesNotExist_thenReturnRecordNotFound() {

        // Given
        String code = "AFN";
        given(systemRepository.findByCode(code)).willReturn(Optional.empty());

        // When
        Throwable throwable = catchThrowable(() -> adaasSystemService.getAdaasSystem(code));

        // Then
        assertThat(throwable).isExactlyInstanceOf(RecordNotFoundException.class)
                .extracting("message").isEqualTo(code + " not found");
        verify(systemRepository).findByCode(code);
    }

    @Test
    void update_givenUpdateSuccessful_thenUpdatedRecordReturned() throws Exception {

        // Given
        String code = "AFN";
        SystemEntity existingSystemEntity = SystemEntity
                .builder()
                .code(code)
                .description("desc")
                .name("name")
                .build();

        given(systemRepository.findByCode(code)).willReturn(Optional.of(existingSystemEntity));

        CreateUpdateSystemRequest createUpdateSystemRequest = CreateUpdateSystemRequest
                .builder()
                .code(code)
                .description("new desc")
                .name("new name")
                .build();
        SystemEntity systemEntityToBeUpdated = SystemEntity
                .builder()
                .code(code)
                .description("new desc")
                .name("new name")
                .build();
        given(systemRepository.save(any(SystemEntity.class)))
                .willReturn(systemEntityToBeUpdated);

        // When
        SystemResponse systemResponse =
                adaasSystemService.update(code, createUpdateSystemRequest);

        // Then
        verify(systemRepository).save(any(SystemEntity.class));
        assertThat(systemResponse.getCode()).isEqualTo(systemEntityToBeUpdated.getCode());
        assertThat(systemResponse.getName()).isEqualTo(systemEntityToBeUpdated.getName());
        assertThat(systemResponse.getDescription()).isEqualTo(systemEntityToBeUpdated.getDescription());
    }

    @Test
    void update_givenNoExistingRecord_thenUpdatedFailed() {

        // Given
        String code = "AFN";
        given(systemRepository.findByCode(code)).willReturn(Optional.empty());

        CreateUpdateSystemRequest createUpdateSystemRequest = CreateUpdateSystemRequest
                .builder()
                .code(code)
                .description("new desc")
                .name("new name")
                .build();

        // When
        assertThrows(RecordNotFoundException.class,
                () -> adaasSystemService.update(code, createUpdateSystemRequest));

        // Then
        verify(systemRepository, times(0)).save(any(SystemEntity.class));


    }

    @Test
    void delete_givenRecordExist_thenDeleteSuccessful() throws Exception {

        // Given
        String code = "AFN";
        SystemEntity existingSystemEntity = SystemEntity
                .builder()
                .id(UUID.randomUUID())
                .code(code)
                .description("desc")
                .name("name")
                .build();

        given(systemRepository.findByCode(code)).willReturn(Optional.of(existingSystemEntity));
        willDoNothing().given(systemRepository).deleteById(any(UUID.class));

        // When
        adaasSystemService.deleteByCode(code);

        // Then
        verify(systemRepository).deleteById(any(UUID.class));
    }

    @Test
    void delete_givenNoExistingRecord_thenDeleteFailed() {

        // Given
        String code = "AFN";
        given(systemRepository.findByCode(code)).willReturn(Optional.empty());

        // When
        assertThrows(RecordNotFoundException.class, () -> adaasSystemService.deleteByCode(code));

        // Then
        verify(systemRepository, times(0)).deleteById(any(UUID.class));
    }


}