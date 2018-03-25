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
        log.debug("--------->{}",nextRecord[0]);
        log.debug("--------->{}",nextRecord[2]);
        log.debug("--------->{}",nextRecord[3]);
        log.debug("--------->{}",nextRecord[4]);
        log.debug("--------->{}",nextRecord[5]);
        log.debug("--------->{}",nextRecord[6]);
        log.debug("--------->{}",nextRecord[7]);
        log.debug("--------->{}",nextRecord[8]);
        log.debug("--------->{}",nextRecord[9]);

        //districtDTO.setId(Long.valueOf(nextRecord[0]));
        districtDTO.setDecimalNumber(Long.valueOf(nextRecord[0]));
        districtDTO.setRomanNumber(nextRecord[1]);
        districtDTO.setDistrictHead(nextRecord[2]);
        districtDTO.setEntityFirstPlace(nextRecord[3]);
        districtDTO.setEntitySecondPlace(nextRecord[4]);

        districtDTO.setTotalFirstPlace(Long.valueOf(nextRecord[5]));
        districtDTO.setTotalSecondPlace(Long.valueOf(nextRecord[6]));
        districtDTO.setTotalVotes(Long.valueOf(nextRecord[7]));
        //districtDTO.setElectoralRoll(Long.valueOf(nextRecord[9]));
        districtDTO.setPublished(true);
        districtDTO.setCreatedDate(ZonedDateTime.now());
        districtDTO.setUpdatedDate(ZonedDateTime.now());
        districtDTO.setElectionId(2L);



        return districtDTO;
    }
}
