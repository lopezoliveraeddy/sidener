package electorum.sidener.service.util;

import electorum.sidener.domain.Election;
import electorum.sidener.domain.PollingPlace;
import electorum.sidener.service.dto.*;
import electorum.sidener.web.rest.PollingPlaceResource;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RecountDemand {
    private final Logger log = LoggerFactory.getLogger(PollingPlaceResource.class);

    public void generateNulityDemand(ElectionDTO electionDTO,  DistrictDTO districtDTO, File file, String filename) throws IOException{
        log.debug("---> filename {}", file);


        FileOutputStream out = new FileOutputStream(new File("/Desarrollo/files/demandas/"+filename));
        try {
            /** ENCABEZADO */
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun encabezado = paragraph.createRun();
            paragraph.setAlignment(ParagraphAlignment.RIGHT);
            encabezado.setBold(true);
            encabezado.setText("DEMANDA DE INCONFORMIDAD");


            XWPFParagraph paragraphA = document.createParagraph();
            paragraphA.setAlignment(ParagraphAlignment.BOTH);
            XWPFRun entrada = paragraphA.createRun();
            entrada.setBold(true);
            entrada.setText(electionDTO.getRecountElectoralInstitute().toUpperCase());
            entrada.addCarriageReturn();
            entrada.setText("PRESENTE.-");
            entrada.addCarriageReturn();
            entrada.addCarriageReturn();

            XWPFRun introduccion = paragraphA.createRun();
            introduccion.setText(electionDTO.getNameDemandant()+" en mi calidad de representante de "+electionDTO.getPoliticalPartyAssociatedName()+" registrado formalmente ante el Consejo Distrital "+districtDTO.getRomanNumber()+" con cabecera en "+districtDTO.getDistrictHead()+", señalo como domicilio para oír y recibir notificaciones el ubicado en ");
            XWPFRun entradaDireccion = paragraphA.createRun();
            entradaDireccion.setColor("FF0000");
            entradaDireccion.setText("UBICACION");

            XWPFRun entradaIntertexto = paragraphA.createRun();
            entradaIntertexto.setColor("000000");
            entradaIntertexto.setText(" y autorizo para esos efectos a ");

            XWPFRun autorizados = paragraphA.createRun();
            autorizados.setColor("FF0000");
            autorizados.setText("Nombre de los autorizados.");
            autorizados.addCarriageReturn();

            XWPFRun fundamento = paragraphA.createRun();
            fundamento.setText("De igual manera, con fundamento en "+electionDTO.getRecountFundamentRequest()+". En contra de los resultados del Cómputo Distrital de la Elección de "+electionDTO.getState()+" "+electionDTO.getElectionTypeName()+" 2015-2016, efectuados por el Consejo distrital con cabecera en  "+districtDTO.getDistrictHead()+", en las casillas que se precisan en la presente demanda.");
            fundamento.addCarriageReturn();
            fundamento.setText("Hago valer mi impugnación y pretensión, en los hechos, agravios y pruebas que a continuación se expresan.");



            XWPFParagraph paragraphB = document.createParagraph();
            XWPFRun hechosTitulo = paragraphB.createRun();
            paragraphB.setAlignment(ParagraphAlignment.CENTER);
            hechosTitulo.setBold(true);
            hechosTitulo.setText("I.   HECHOS");

            XWPFParagraph paragraphC = document.createParagraph();
            XWPFRun hechosContenido = paragraphC.createRun();

            hechosContenido.setText("1.     El 06 de Octubre de 2015, inició al proceso electoral ordinario para renovar Gobernador." +
                "2.     El 05 de Junio de 2016, se llevó acabo la jornada electoral, registrándose diversas irregularidades en la votación recibida en las casillas del Distrito I, las cuales se precisan y se impugnan." +
                "3.     El 08 de Junio de 2016, se llevaron a cabo los cómputos distritales en la entidad antes mencionada, en específico en el Consejo, concluyendo el correspondiente a la elección de undefined el 15 de Junio de 2016.");








            document.write(out);
            out.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


    }

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


            introduccion.setText(electionDTO.getNameDemandant() +" en mi calidad de representante del "+electionDTO.getPoliticalPartyAssociatedName()+", registrado formalmente ante este Consejo Distrital, en la presente sesión de cómputo Distrital de la elección de "+electionDTO.getElectionTypeName()+", con fundamento en lo dispuesto en "+electionDTO.getRecountFundamentRequest()+", me permito solicitar a usted lo siguiente:");
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
                Double diferenciaPorcentual = (((double)actualDistrict.getTotalFirstPlace()- (double)actualDistrict.getTotalSecondPlace())/(double)actualDistrict.getTotalVotes())*100;

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
            despedida.setText("Representante, "+electionDTO.getPoliticalPartyAssociatedName());



            document.write(out);
            out.close();



        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void generateRecountDemandPollingPlace(List<PollingPlaceDTO> pollingPlaceDTOList, ElectionDTO electionDTO, String filename) throws IOException {
        log.debug("---> filename {}", filename);
        //log.debug(districtDTOS.toString());

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


            introduccion.setText(electionDTO.getNameDemandant() +" en mi calidad de representante del "+electionDTO.getPoliticalPartyAssociatedName()+", registrado formalmente ante este Consejo Distrital, en la presente sesión de cómputo Distrital de la elección de "+electionDTO.getElectionTypeName()+", con fundamento en lo dispuesto en "+electionDTO.getRecountFundamentRequest()+", me permito solicitar a usted lo siguiente: ");
            introduccion.addCarriageReturn();
            introduccion.addCarriageReturn();

            encabezado.setBold(true);
            encabezado.setCapitalized(true);
            encabezado.addBreak();
            log.debug("electionDTO.getRecountElectoralInstitute().isEmpty() {}", electionDTO.getRecountElectoralInstitute());

            if(  electionDTO.getRecountElectoralInstitute() == null){
                electionDTO.setRecountElectoralInstitute("INSTITUTO NACIONAL ELECTORAL");
            }

            encabezado.setText(electionDTO.getRecountElectoralInstitute().toUpperCase());
            encabezado.addBreak();
            encabezado.addBreak();
            encabezado.setText("PRESENTE. -");
            encabezado.addCarriageReturn();
            encabezado.addCarriageReturn();

            solicitud.setText("Se realice el recuento de votos de las casillas que a continuación se indican ");
            regla.setBold(true);
            regla.setText("por encuadrar los supuestos normativos siguientes:\n");
            regla.addCarriageReturn();


            for (int i= 0; i < pollingPlaceDTOList.size(); i++){
                PollingPlaceDTO pollingPlaceDTO = pollingPlaceDTOList.get(i);
                String tipo = "";

                if(pollingPlaceDTO.getTypePollingPlace().toString() == "BASIC"){
                    tipo = "Básica";
                }
                if(pollingPlaceDTO.getTypePollingPlace().toString() == "CONTIGUOUS"){
                    tipo = "Contigua";
                }
                if(pollingPlaceDTO.getTypePollingPlace().toString() == "EXTRAORDINARY"){
                    tipo = "Extraordinaria";
                }
                if(pollingPlaceDTO.getTypePollingPlace().toString() == "SPECIAL"){
                    tipo = "Especial";
                }

                log.debug("---> pollingPlaceDTO {}",pollingPlaceDTO);

                XWPFParagraph contenidoCasillas = document.createParagraph();
                XWPFRun tituloCasilla = contenidoCasillas.createRun();
                tituloCasilla.setBold(true);
                contenidoCasillas.setAlignment(ParagraphAlignment.CENTER);

                tituloCasilla.setText(pollingPlaceDTO.getTown());
                tituloCasilla.addCarriageReturn();
                tituloCasilla.addCarriageReturn();
                tituloCasilla.setText(" Sección: "+ pollingPlaceDTO.getSection()+", "+tipo+ " "+ pollingPlaceDTO.getTypeNumber());
                tituloCasilla.addCarriageReturn();
                Set<CausalDTO> causalDTOS = pollingPlaceDTO.getCausals();

                for(CausalDTO causalDTO : causalDTOS){
                    log.debug("causal DTO {}", causalDTO);
                    XWPFRun causalCasilla = contenidoCasillas.createRun();

                    Set<CausalDescriptionDTO> causalDescriptionDTOS = causalDTO.getCausalDescriptions();
                    String cadenaDescripcion = "";
                    for(CausalDescriptionDTO causalDescriptionDTO : causalDescriptionDTOS){
                        cadenaDescripcion += causalDescriptionDTO.getText();
                    }
                    causalCasilla.setText(causalDTO.getName()+". "+cadenaDescripcion);
                    if(causalDTO.getId() == 4){
                        XWPFTable table = document.createTable();
                        XWPFRun textoTabla = paragraphTwo.createRun();
                        textoTabla.setCapitalized(true);
                        textoTabla.addCarriageReturn();
                        Double diferenciaPorcentual = ((double)pollingPlaceDTO.getTotalFirstPlace()- (double)pollingPlaceDTO.getTotalSecondPlace())/(double)pollingPlaceDTO.getTotalVotes();
                        log.debug("PRIMER LUGAR {}",pollingPlaceDTO.getTotalFirstPlace());
                        log.debug("SEGUNDO LUGAR {}", pollingPlaceDTO.getTotalSecondPlace());
                        log.debug("TOTAL DE VOTOS {}",pollingPlaceDTO.getTotalVotes());
                        log.debug("diferenciaPorcentual ----> {}",diferenciaPorcentual);
                        log.debug("RESTA ----> {}",((double)pollingPlaceDTO.getTotalFirstPlace()- (double)pollingPlaceDTO.getTotalSecondPlace())/(double)pollingPlaceDTO.getTotalVotes());
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
                        tableRowThree.getCell(0).setText(pollingPlaceDTO.getEntityFirstPlace());
                        tableRowThree.getCell(1).setText(pollingPlaceDTO.getTotalFirstPlace().toString());
                        tableRowThree.getCell(2).setText(pollingPlaceDTO.getEntitySecondPlace());
                        tableRowThree.getCell(3).setText(pollingPlaceDTO.getTotalSecondPlace().toString());
                        tableRowThree.getCell(4).setText(String.valueOf( new DecimalFormat("#.##").format(diferenciaPorcentual)));





                    }
                    log.debug("--- CAUSAL DTO--- {}",causalDTO.toString());
                    causalCasilla.addCarriageReturn();
                    causalCasilla.addBreak();
                }
            }

            XWPFParagraph paragraphThree = document.createParagraph();
            XWPFRun despedida = paragraphThree.createRun();
            despedida.setText("Asimismo, en caso de que del resultado del cómputo distrital resultara que la diferencia porcentual entre el primer y segundo lugar fuera igual o menor a un punto porcentual, se solicita que ese consejo distrital realizar el recuento de votos en la totalidad de las casillas correspondientes a dicho Distrito Electoral I con cabecera en ACATLAN DE PEREZ FIGUEROA.");
            despedida.addCarriageReturn();
            despedida.addCarriageReturn();
            despedida.setText("ATENTAMENTE");
            despedida.addCarriageReturn();
            despedida.addCarriageReturn();
            despedida.addCarriageReturn();
            despedida.setText(electionDTO.getNameDemandant());
            despedida.addCarriageReturn();
            despedida.setText("Representante, "+electionDTO.getPoliticalPartyAssociatedName());



            document.write(out);
            out.close();



        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }

}
