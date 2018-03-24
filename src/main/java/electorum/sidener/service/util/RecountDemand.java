package electorum.sidener.service.util;

import electorum.sidener.service.dto.DistrictDTO;
import electorum.sidener.service.dto.ElectionDTO;
import electorum.sidener.web.rest.PollingPlaceResource;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RecountDemand {
    private final Logger log = LoggerFactory.getLogger(PollingPlaceResource.class);

    public void generateRecountDemand(List<DistrictDTO> districtDTOS, ElectionDTO electionDTO, String filename) throws IOException {
        log.debug("---> filename {}", filename);
        log.debug(districtDTOS.toString());

        FileOutputStream out = new FileOutputStream(new File("/Desarrollo/files/demandas/"+filename));
        try{
            XWPFDocument  document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun encabezado = paragraph.createRun();

            XWPFParagraph paragraphTwo = document.createParagraph();
            paragraphTwo.setAlignment(ParagraphAlignment.BOTH);
            XWPFRun introduccion = paragraphTwo.createRun();

            XWPFRun solicitud = paragraphTwo.createRun();
            XWPFRun regla = paragraphTwo.createRun();


            introduccion.setText(electionDTO.getNameDemandant() +" en mi calidad de representante del "+electionDTO.getPoliticalPartyAsociatedName()+", registrado formalmente ante este Consejo Distrital, en la presente sesión de cómputo Distrital de la elección de "+electionDTO.getElectionTypeName()+", con fundamento en lo dispuesto en "+electionDTO.getRecountFundamentRequest()+", me permito solicitar a usted lo siguiente:");
            introduccion.addCarriageReturn();
            introduccion.addCarriageReturn();

            encabezado.setBold(true);
            encabezado.setCapitalized(true);
            encabezado.addBreak();
            encabezado.setText(electionDTO.getRecountElectoralInstitute().toUpperCase());
            encabezado.addBreak();
            encabezado.addBreak();
            encabezado.setText("PRESENTE. -");
            encabezado.addCarriageReturn();
            encabezado.addCarriageReturn();

            solicitud.setText("Se solicita realizar el recuento de votos en la totalidad de las casillas correspondientes los siguientes Distritos Electorales");
            regla.setBold(true);
            regla.setText(" al existir una diferencia menor a un punto porcentual entre el primer y segundo lugar, tal y como se observa en la tabla siguiente:\n");
            regla.addCarriageReturn();

            for (int i = 0; i < districtDTOS.size(); i++) {
                DistrictDTO actualDistrict  = districtDTOS.get(i);

                XWPFParagraph paragraphTitle = document.createParagraph();
                XWPFRun tituloDistrito = paragraphTitle.createRun();
                tituloDistrito.setBold(true);
                tituloDistrito.setText("Distrito "+actualDistrict.getRomanNumber()+" "+actualDistrict.getDistrictHead());
                XWPFTable table = document.createTable();
                XWPFRun textoTabla = paragraphTwo.createRun();
                textoTabla.setCapitalized(true);
                textoTabla.addCarriageReturn();
                Long diferenciaPorcentual = (actualDistrict.getTotalFirstPlace()- actualDistrict.getTotalSecondPlace())/actualDistrict.getTotalVotes();

                //create first row
                XWPFTableRow tableRowOne = table.getRow(0);
                tableRowOne.getCell(0).setText("PRIMER LUGAR");
                tableRowOne.addNewTableCell().setText("");
                tableRowOne.addNewTableCell().setText("SEGUNDO LUGAR");
                tableRowOne.addNewTableCell().setText("");
                tableRowOne.addNewTableCell().setText("");

                //create second row
                XWPFTableRow tableRowTwo = table.createRow();
                tableRowTwo.getCell(0).setText("Patido o Coalición");
                tableRowTwo.getCell(1).setText("Votación");
                tableRowTwo.getCell(2).setText("Partido o Coalición");
                tableRowTwo.getCell(3).setText("Votación");
                tableRowTwo.getCell(4).setText("Diferencia Porcentual");

                //create third row
                XWPFTableRow tableRowThree = table.createRow();
                tableRowThree.getCell(0).setText(actualDistrict.getEntityFirstPlace());
                tableRowThree.getCell(1).setText(actualDistrict.getTotalFirstPlace().toString());
                tableRowThree.getCell(2).setText(actualDistrict.getEntitySecondPlace());
                tableRowThree.getCell(3).setText(actualDistrict.getTotalSecondPlace().toString());
                tableRowThree.getCell(4).setText(String.valueOf(diferenciaPorcentual));

                document.createParagraph().createRun().addBreak();
            }

            XWPFParagraph paragraphThree = document.createParagraph();
            XWPFRun despedida = paragraphThree.createRun();
            despedida.addCarriageReturn();
            despedida.addCarriageReturn();
            despedida.setText("ATENTAMENTE");
            despedida.addCarriageReturn();
            despedida.addCarriageReturn();
            despedida.addCarriageReturn();
            despedida.setText(electionDTO.getNameDemandant());
            despedida.addCarriageReturn();
            despedida.setText("Representante, "+electionDTO.getPoliticalPartyAsociatedName());



            document.write(out);
            out.close();



        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
