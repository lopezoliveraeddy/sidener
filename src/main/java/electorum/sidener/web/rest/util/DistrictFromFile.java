package electorum.sidener.web.rest.util;

import com.opencsv.CSVReader;
import electorum.sidener.service.dto.DistrictDTO;
import electorum.sidener.web.rest.DistrictResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DistrictFromFile {
    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);

    /**
     *
     * @param csvFile
     * @param eleccion
     * @return
     */
    public List<DistrictDTO> processFile(CSVReader csvFile, Long eleccion) {
        List<DistrictDTO> districtDTOList = new ArrayList<>();
        String[] nextRecord = null;
        try {
            while ((nextRecord = csvFile.readNext()) != null) {
                districtDTOList.add(this.districtInfo(nextRecord));
            }

        } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

        return districtDTOList;
    }

    /**
     *
     * @param nextRecord
     * @return
     */
    private DistrictDTO districtInfo(String[] nextRecord) {
        DistrictDTO districtDTO = new DistrictDTO();
        //districtDTO.setId(Long.valueOf(nextRecord[0]));
        districtDTO.setDecimalNumber(Long.valueOf(nextRecord[1]));
        districtDTO.setRomanNumber(nextRecord[2]);
        districtDTO.setDistrictHead(nextRecord[3]);
        districtDTO.setEntityFirstPlace("");
        districtDTO.setTotalFirstPlace(0L);
        districtDTO.setTotalSecondPlace(0L);
        districtDTO.setTotalVotes(Long.valueOf(nextRecord[8]));
        districtDTO.setElectoralRoll(Long.valueOf(nextRecord[9]));
        districtDTO.setPublished(Boolean.valueOf(nextRecord[10]));
        districtDTO.setCreatedDate(ZonedDateTime.now());
        districtDTO.setUpdatedDate(ZonedDateTime.now());
        districtDTO.setElectionId(Long.valueOf(nextRecord[14]));



        return districtDTO;
    }
}
