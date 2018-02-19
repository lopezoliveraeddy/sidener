package electorum.sidener.service.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import electorum.sidener.service.DistrictService;
import electorum.sidener.service.ElectionService;
import electorum.sidener.service.dto.DistrictDTO;
import electorum.sidener.service.dto.ElectionDTO;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
/*
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
*/

public class Documents {


    public void generateWord(DistrictDTO districtDTO, ElectionDTO electionDTO, String filename) throws IOException {

		FileOutputStream out = new FileOutputStream(new File("/files/"+filename));
		try {
			XWPFDocument  document = new XWPFDocument();
			XWPFParagraph paragraph = document.createParagraph();
			XWPFRun encabezado = paragraph.createRun();
            XWPFParagraph paragraphTwo = document.createParagraph();
            XWPFTable table = document.createTable();
            XWPFParagraph paragraphThree = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.LEFT);
            paragraphTwo.setAlignment(ParagraphAlignment.BOTH);
            XWPFRun introduccion = paragraphTwo.createRun();
            XWPFRun solicitud = paragraphTwo.createRun();
            XWPFRun regla = paragraphTwo.createRun();
            XWPFRun despedida = paragraphThree.createRun();
            paragraphThree.setAlignment(ParagraphAlignment.CENTER);
            Long diferenciaPorcentual = (districtDTO.getTotalFirstPlace()- districtDTO.getTotalSecondPlace())/districtDTO.getTotalVotes();


            encabezado.setBold(true);
            encabezado.setCapitalized(true);
            encabezado.setText("CONSEJO DISTRITAL "+ districtDTO.getRomanNumber());
            encabezado.addBreak();
            encabezado.setText("DEL "+electionDTO.getRecountElectoralInstitute().toUpperCase());
            encabezado.addBreak();
            encabezado.setText("CON CABECERA EN "+districtDTO.getDistrictHead());
            encabezado.addBreak();
            encabezado.setText("PRESENTE. -");
            encabezado.addCarriageReturn();
            encabezado.addCarriageReturn();
            introduccion.setText(electionDTO.getNameDemandant() +" en mi calidad de representante del "+electionDTO.getPoliticalPartyAsociatedName()+", registrado formalmente ante este Consejo Distrital, en la presente sesión de cómputo Distrital de la elección de "+electionDTO.getElectionTypeName()+", con fundamento en lo dispuesto en "+electionDTO.getRecountFundamentRequest()+", me permito solicitar a usted lo siguiente:");
            introduccion.addCarriageReturn();
            introduccion.addCarriageReturn();
            solicitud.setText("Se solicita realizar el recuento de votos en la totalidad de las casillas correspondientes al Distrito Electoral "+districtDTO.getRomanNumber()+" con cabecera en "+districtDTO.getDistrictHead());
            regla.setBold(true);
            regla.setText(" al existir una diferencia menor a un punto porcentual entre el primer y segundo lugar, tal y como se observa en la tabla siguiente:\n");
            regla.addCarriageReturn();
            regla.addCarriageReturn();
            regla.addCarriageReturn();

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
            tableRowThree.getCell(0).setText(districtDTO.getEntityFirstPlace());
            tableRowThree.getCell(1).setText(districtDTO.getTotalFirstPlace().toString());
            tableRowThree.getCell(2).setText(districtDTO.getEntitySecondPlace());
            tableRowThree.getCell(3).setText(districtDTO.getTotalSecondPlace().toString());
            tableRowThree.getCell(4).setText(String.valueOf(diferenciaPorcentual));
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


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
