package electorum.sidener.web.rest.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.CSVReader;

import electorum.sidener.service.dto.PollingPlaceDTO;
import electorum.sidener.service.dto.WinnersDTO;
import electorum.sidener.web.rest.PollingPlaceResource;


/**
 * @author eddy
 *
 */
/**
 * @author eddy
 *
 */
public class ElectionFromFile {
	private static final String SEPARADORDATOS = "S1";
	private static final String SEPARADORPARTIDOS = "S2";
	private static final String SEPARADORCOALICIONES="S3";
	private static final String SEPARADORCANDIDATOSIND = "S4";
	private static final String PRIMERLUGAR = "primerLugar";
	private static final String SEGUNDOLUGAR = "segundoLugar";
	private final Logger log = LoggerFactory.getLogger(PollingPlaceResource.class);

	/**
	 * @param csvFile
	 * @param eleccion
	 * @return
	 */
	public List<PollingPlaceDTO> processFile(CSVReader csvFile, Long eleccion) {
		// TODO Auto-generated method stub

		List<PollingPlaceDTO> pollingPlaceDTOList = new ArrayList<>();
		String[] nextRecord = null;
		try {
			List<String> partidos    = new ArrayList<String>();
			List<String> coaliciones = new ArrayList<String>();
			List<String> candidatosi = new ArrayList<String>();

			nextRecord = csvFile.readNext();

			int marcaDatos = ArrayUtils.indexOf(nextRecord, SEPARADORDATOS);
			int marcaPartidos =ArrayUtils.indexOf(nextRecord, SEPARADORPARTIDOS);
			int marcaCoaliciones=ArrayUtils.indexOf(nextRecord, SEPARADORCOALICIONES);
			int marcaCandidatos  = ArrayUtils.indexOf(nextRecord, SEPARADORCANDIDATOSIND);

			partidos = this.processHeader(nextRecord,marcaDatos,marcaPartidos);
			coaliciones = this.processHeader(nextRecord,marcaPartidos, marcaCoaliciones);
			candidatosi = this.processHeader(nextRecord,marcaCoaliciones,marcaCandidatos);
			while ((nextRecord = csvFile.readNext()) != null) {
				pollingPlaceDTOList.add(this.processPollingPlaceInfo(nextRecord, eleccion,partidos,marcaDatos, marcaPartidos, marcaCoaliciones, marcaCandidatos)) ;
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return pollingPlaceDTOList;

	}


	/**
	 * @param nextRecord
	 * @param eleccion
	 * @param partidos
	 * @param marcaDatos
	 * @param marcaPartidos
	 * @param marcaCoaliciones
	 * @param marcaCandidatos
	 * @return
	 */
	@SuppressWarnings("null")
	@Autowired
	private PollingPlaceDTO processPollingPlaceInfo(String[] nextRecord, Long eleccion, List<String> partidos, int marcaDatos, int marcaPartidos, int marcaCoaliciones, int marcaCandidatos) {

		// TODO Auto-generated method stub
		Map<String, WinnersDTO> ganadoresPartidos = new HashMap<String, WinnersDTO>();
		ganadoresPartidos = this.processWinnersByPollingPlace(nextRecord, partidos, marcaDatos,marcaPartidos);
		PollingPlaceDTO pollingPlaceDTO = new PollingPlaceDTO();
		pollingPlaceDTO.setDistrictId(Long.valueOf(nextRecord[0]));
		pollingPlaceDTO.setSection(nextRecord[1]);
		pollingPlaceDTO.setTown(nextRecord[2]);
		pollingPlaceDTO.setTypePollingPlace(null);
		pollingPlaceDTO.setAddress(nextRecord[4]);
		pollingPlaceDTO.setLeftoverBallots(Long.valueOf(nextRecord[5]));
		pollingPlaceDTO.setVotingCitizens(Long.valueOf(nextRecord[6]));
		pollingPlaceDTO.setTotalVotes((Long.valueOf(nextRecord[8])));
		pollingPlaceDTO.setElectionId(Long.parseLong(nextRecord[9]));

		pollingPlaceDTO.setEntityFirstPlace(ganadoresPartidos.get(PRIMERLUGAR).getPartido());
		pollingPlaceDTO.setEntitySecondPlace(ganadoresPartidos.get(SEGUNDOLUGAR).getPartido());

		pollingPlaceDTO.setTotalFirstPlace(ganadoresPartidos.get(PRIMERLUGAR).getCantidad());
		pollingPlaceDTO.setTotalSecondPlace(ganadoresPartidos.get(SEGUNDOLUGAR).getCantidad());

//		log.debug("CASILLA : {} ",pollingPlaceDTO.toString());
		return pollingPlaceDTO;

	}

	/**
	 * @param nextRecord
	 * @param partidos
	 * @param inicioResultados
	 * @param finResultados
	 * @return
	 */
	private Map<String, WinnersDTO> processWinnersByPollingPlace(String[] nextRecord, List<String> partidos, int inicioResultados, int finResultados) {
		// TODO Auto-generated method stub
//		log.debug("ENTRA A processWinnersByPollingPlace ---->");
//		log.debug("INICIO : {} ", inicioResultados);
//		log.debug("FIN : {} ", finResultados);
		 Long primerLugar = -1L;
		 Long segundoLugar = -1L;
		 String partidoPrimerLugar = "";
		 String partidoSegundoLugar = "";
		 WinnersDTO winnersDTOPrimerLugar = new WinnersDTO();
		 WinnersDTO winnersDTOSegundoLugar = new WinnersDTO();
		 Map<String, WinnersDTO> winnersByPollingPlace = new HashMap<String, WinnersDTO>();
		 int k = 0;

		 for (int i = inicioResultados+1; i < finResultados; i++) {
			log.debug("VOTOS : {}", nextRecord[i] );
			if( primerLugar < Integer.parseInt(nextRecord[i]) ) {
				segundoLugar = primerLugar;
				primerLugar  = Long.parseLong(nextRecord[i]);

				partidoSegundoLugar = partidoPrimerLugar;
				partidoPrimerLugar = partidos.get(k);

			}else {
				/*si el valor no es mayor al primerLugar*/
				if(Integer.parseInt(nextRecord[i]) > segundoLugar) {
					segundoLugar = Long.parseLong(nextRecord[i]);
					partidoSegundoLugar = partidos.get(k);
				}
			}
			k++;
		}
//		 log.debug("PRIMER LUGAR PARTIDO : {}", partidoPrimerLugar);
//		 log.debug("TOTAL PRIMER LUGAR : {}", primerLugar);
//		 log.debug("SEGUNDO LUGAR PARTIDO : {}", partidoSegundoLugar);
//		 log.debug("TOTAL SEGUNDO LUGAR : {}", segundoLugar);
		 winnersDTOPrimerLugar.setPartido(partidoPrimerLugar);
		 winnersDTOPrimerLugar.setCantidad(primerLugar);

		 winnersDTOSegundoLugar.setPartido(partidoSegundoLugar);
		 winnersDTOSegundoLugar.setCantidad(segundoLugar);

		 winnersByPollingPlace.put(PRIMERLUGAR, winnersDTOPrimerLugar);
		 winnersByPollingPlace.put(SEGUNDOLUGAR, winnersDTOSegundoLugar);

		return winnersByPollingPlace;
	}

	/**
	 * @param nextRecord
	 * @param start
	 * @param end
	 * @return
	 */
	private List<String> processHeader(String[] nextRecord, int start , int end) {
		List<String> vector    = new ArrayList<String>();
		int topeDatos = ArrayUtils.indexOf(nextRecord, start);
		int topePartidos = ArrayUtils.indexOf(nextRecord, end);

		for(int k = start+1 ; k<= end-1 ; k++) {
			vector.add(nextRecord[k]);
		}
		//log.debug("VECTOR : {}", vector.toString());
		return vector;
	}



}

