package com.gaia.results.demo.controllers;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class UploadController {


    @GetMapping("/")
    public String index() {
        return "index";
    }

    public <T> List<T> loadObjectList(Class<T> type, String fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            File file = new ClassPathResource(fileName).getFile();
            MappingIterator<T> readValues =
                    mapper.reader(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            //logger.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }
    CsvSchema schema;
    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {


        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `images` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(file.getInputStream(), ByteOrderMark.UTF_8)))) {

                String fileName = "test.csv";
                File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
                file.transferTo(convFile);
                CsvMapper mapper = new CsvMapper();
                schema = CsvSchema.emptySchema().withHeader(); // use first row as header; otherwise defaults are fine
                MappingIterator<Map<String,String>> it = mapper.readerFor(Map.class)
                        .with(schema)
                        .readValues(convFile);

                Map<Integer,Map<String,String>> rowAsMapList = new HashedMap();
                Integer row_count = 0;
                while (it.hasNext()) {
                    rowAsMapList.put(row_count,it.next());
                    row_count++;
                    //System.out.println(it.next());
                    // access by column name, as defined in the header row...
                }

                model.addAttribute("gaia_results", rowAsMapList);
                model.addAttribute("status", true);



            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }

        return "file-upload-status";
    }

    @GetMapping("/export-results")
    public void exportCSV(HttpServletResponse response) throws Exception {

        //set file name and content type
        String filename = "image_compare_results.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");


    }
}
