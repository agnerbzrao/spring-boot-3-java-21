package br.com.spring.agner.rest_with_spring_boot.file.exporter.contract;

import br.com.spring.agner.rest_with_spring_boot.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;
}