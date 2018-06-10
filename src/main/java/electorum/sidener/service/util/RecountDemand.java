package electorum.sidener.service.util;

import electorum.sidener.domain.Election;
import electorum.sidener.domain.PollingPlace;
import electorum.sidener.service.dto.*;
import electorum.sidener.web.rest.PollingPlaceResource;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.AttributeList;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

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

    /**
     *
     * @param pollingPlaceDTOList
     * @param electionDTO
     * @param filename
     * @param district
     * @param listaTratadaCausales
     * @throws IOException
     */

    public void generateRecountDemandPollingPlace(List<PollingPlaceDTO> pollingPlaceDTOList, ElectionDTO electionDTO, String filename, DistrictDTO district, Map<Integer, CausalPollingRelationDTO> listaTratadaCausales) throws IOException {
        String partyOrCoalitionOrCandidate = "";
        String nameDemandant = "";
        String electionTypeName = "";
        String recountFundamentRequest = "";
        String recountElectoralInstitute = "";
        ArrayList<PollingPlaceWithRecountDTO> listPollingPlaceWithRecount = new ArrayList<PollingPlaceWithRecountDTO>() ;
        //Map<Long, ArrayList<CausalDTO>> listadoCausales  = new HashMap<Long, ArrayList<CausalDTO>>();
        int i = 1;
        int j = 0;

        String[] romanos = {"I","II","III","IV","V","VI","VII","VIII","IX", "X", "XI","XII","XIII","XIV","XV","XVI","XVII","XVIII","XIX","XX"};
        Long [][] relaciones = new Long  [30][];

       // List<Long> casillasConCausal= new ArrayList<Long>();




        for (PollingPlaceDTO pollingPlaceDTO : pollingPlaceDTOList){

            if(pollingPlaceDTO.getNullVotes() > (pollingPlaceDTO.getTotalFirstPlace()- pollingPlaceDTO.getTotalSecondPlace()) ){
                PollingPlaceWithRecountDTO  pollingPlaceWithRecountDTO = new PollingPlaceWithRecountDTO();
                pollingPlaceWithRecountDTO.setDiferenceFirstSecond( pollingPlaceDTO.getTotalFirstPlace()- pollingPlaceDTO.getTotalSecondPlace() );
                pollingPlaceWithRecountDTO.setId(pollingPlaceDTO.getId());
                pollingPlaceWithRecountDTO.setNullVotes(pollingPlaceDTO.getNullVotes());
                pollingPlaceWithRecountDTO.setSection(pollingPlaceDTO.getSection());
                pollingPlaceWithRecountDTO.setTypeNumber(pollingPlaceDTO.getTypeNumber());
                pollingPlaceWithRecountDTO.setTypePollingPlace(pollingPlaceDTO.getTypePollingPlace());
                listPollingPlaceWithRecount.add(pollingPlaceWithRecountDTO);

            }
            if(pollingPlaceDTO.getCausals() != null){
                for (CausalDTO causalDTO:pollingPlaceDTO.getCausals()
                    ) {
                    try{
                        // obtenemos el id de causal a insertar
                        Integer idCausal = Math.toIntExact(causalDTO.getId());
                        // se obtiene el espacio de las causales
                        CausalPollingRelationDTO causalPollingRelationDTO = listaTratadaCausales.get(idCausal);
                        // del espacio de causales se obtiene la lista de casillas
                        // no hay nada de casillas
                        if(causalPollingRelationDTO.getPollingPlaceDTOList() == null){

                            List<PollingPlaceDTO> pollingPlaceDTOListInner = new ArrayList<PollingPlaceDTO>();
                            pollingPlaceDTOListInner.add(pollingPlaceDTO);
                            causalPollingRelationDTO.setPollingPlaceDTOList(pollingPlaceDTOListInner);
                        }else{
                            List<PollingPlaceDTO> pollingPlaceDTOListInner = causalPollingRelationDTO.getPollingPlaceDTOList();
                            pollingPlaceDTOListInner.add(pollingPlaceDTO);
                            causalPollingRelationDTO.setPollingPlaceDTOList(pollingPlaceDTOListInner);
                        }

                    }catch (NullPointerException e){
                        System.out.print("Caught the NullPointerException");

                    }






                }

            }





        }

        FileOutputStream out = new FileOutputStream(new File("/Desarrollo/files/demandas/"+filename));
        try{
            XWPFDocument  document = new XWPFDocument();
            XWPFParagraph paragraph = document.createParagraph();

            try{
                if(electionDTO.getPoliticalPartyAssociatedName() != null){
                    partyOrCoalitionOrCandidate = "l partido Político "+ electionDTO.getPoliticalPartyAssociatedName()+" ";
                }
                if(electionDTO.getCoalitionAssociatedName() != null){
                    partyOrCoalitionOrCandidate = " la coalición con nombre \""+ electionDTO.getCoalitionAssociatedName()+"\" ";

                }
                if(electionDTO.getIndependentCandidateAssociatedName() != null){
                    partyOrCoalitionOrCandidate = "l candidato independiente  "+ electionDTO.getIndependentCandidateAssociatedName()+" ";

                }



                electionTypeName = electionDTO.getElectionTypeName();
                nameDemandant = electionDTO.getNameDemandant();
                recountFundamentRequest = electionDTO.getRecountFundamentRequest();
                recountElectoralInstitute = electionDTO.getRecountElectoralInstitute();
                if(  electionDTO.getRecountElectoralInstitute() == null){
                    recountElectoralInstitute = "INSTITUTO NACIONAL ELECTORAL";
                }
            }catch (NullPointerException e){
                System.out.print("Caught the NullPointerException");
            }

            /**
             * CONSEJO DISTRITAL I
             * DEL INSTITUTO Instituto Electoral de Oaxaca CON CABECERA EN ACATLAN DE PEREZ FIGUEROA
             * PRESENTE.-
             * */
            XWPFParagraph paragraphA = document.createParagraph();
            paragraphA.setAlignment(ParagraphAlignment.BOTH);
            XWPFRun entrada = paragraphA.createRun();
            entrada.setBold(true);
            entrada.setText("Consejo Distrital "+district.getRomanNumber());
            entrada.addCarriageReturn();

            entrada.setText("DEL "+recountElectoralInstitute.toUpperCase()+" CON CABECERA DISTRITAL EN "+ district.getDistrictHead());
            entrada.addCarriageReturn();
            entrada.setText("PRESENTE.-");
            entrada.addCarriageReturn();
            entrada.addCarriageReturn();



            XWPFParagraph paragraphTwo = document.createParagraph();
            paragraphTwo.setAlignment(ParagraphAlignment.BOTH);
            XWPFRun introduccion = paragraphTwo.createRun();

            XWPFRun solicitud = paragraphTwo.createRun();




            if ( partyOrCoalitionOrCandidate == ""){
                partyOrCoalitionOrCandidate = "-----";
            }


            /**
             * Juan Palacios García en mi calidad de representante del Partido Coalición con rumbo y estabilidad para Oaxaca,
             * registrado formalmente ante este Consejo Distrital, en la presente sesión de cómputo Distrital de la elección de GOBERNADOR,
             * con fundamento en lo dispuesto en los artículos Artículos 61, 62 y demás relativos y aplicables de  la
             * Ley del Sistema de Medios de Impugnación en Materia Electoral y de Participación Ciudadana para el Estado de Oaxaca.,
             * me permito solicitar a usted lo siguiente:
             *
             */
            introduccion.setText(nameDemandant +" en mi calidad de representante de"+ partyOrCoalitionOrCandidate +", registrado formalmente ante este Consejo Distrital, en la presente sesión de cómputo Distrital de la elección de "+electionTypeName+", con fundamento en lo dispuesto en "+recountFundamentRequest+", me permito solicitar a usted lo siguiente: ");
            introduccion.addCarriageReturn();
            introduccion.addCarriageReturn();



            solicitud.setText("Se realice el recuento de votos de las casillas que a continuación se indican ");
            solicitud.setBold(true);
            solicitud.setText(" que generan duda sobre el resultado de la votación en las casillas que a continuación se enumeran:");

            solicitud.addCarriageReturn();

            /*
            * I.- El número de votos nulos es mayor a la diferencia entre el primer y segundo lugar en las casillas:

             * */

            XWPFParagraph nullVotesSection = document.createParagraph();
            nullVotesSection.setAlignment(ParagraphAlignment.BOTH);
            XWPFRun introNullVotes = nullVotesSection.createRun();
            introNullVotes.setBold(true);
            introNullVotes.setText("I.- El número de votos nulos es mayor a la diferencia entre el primer y segundo lugar en las casillas:");
            XWPFTable tableNullVotes    = document.createTable();

            XWPFTableRow tableRowOne = tableNullVotes.getRow(0);
            tableRowOne.getCell(0).setText("CONSECUTIVO");
            tableRowOne.addNewTableCell().setText("SECCIÓN");
            tableRowOne.addNewTableCell().setText("CASILLA");
            tableRowOne.addNewTableCell().setText("CAUSA DE RECUENTO");


            for (PollingPlaceWithRecountDTO pollingPlaceWithRecount: listPollingPlaceWithRecount ){
                XWPFTableRow tableRowTwo = tableNullVotes.createRow();
                if(pollingPlaceWithRecount.getNullVotes() > pollingPlaceWithRecount.getDiferenceFirstSecond() ){
                    tableRowTwo.getCell(0).setText(String.valueOf(i));
                    tableRowTwo.getCell(1).setText(String.valueOf(pollingPlaceWithRecount.getSection()));
                    tableRowTwo.getCell(2).setText(this.getTipoCasilla(String.valueOf(pollingPlaceWithRecount.getTypePollingPlace()))  +" "+ pollingPlaceWithRecount.getTypeNumber() );
                    tableRowTwo.getCell(3).setText("Hay "+ String.valueOf(pollingPlaceWithRecount.getNullVotes())+" votos nulos y la diferencia entre 1ro y 2do lugar es de "+ String.valueOf(pollingPlaceWithRecount.getDiferenceFirstSecond())  +" votos ");
                    i++;
                }

            }


            Iterator it = listaTratadaCausales.keySet().iterator();

            XWPFParagraph causalsuVotesSection = document.createParagraph();
            causalsuVotesSection.setAlignment(ParagraphAlignment.BOTH);

            while(it.hasNext()){
                Integer key = (Integer) it.next();
                CausalPollingRelationDTO causalPollingRelationDTO = listaTratadaCausales.get(key);
                List<PollingPlaceDTO> pollingPlaceDTOList1 = causalPollingRelationDTO.getPollingPlaceDTOList();
                if(pollingPlaceDTOList1 != null  && pollingPlaceDTOList.size() > 0){
                    try{
                        XWPFRun introCausalsVotes = causalsuVotesSection.createRun();
                        introCausalsVotes.addCarriageReturn();
                        introCausalsVotes.setBold(true);
                        introCausalsVotes.addCarriageReturn();


                        introCausalsVotes.setText( romanos[j+1] + ". "+ causalPollingRelationDTO.getNombreCausal()+" ");

                        for (PollingPlaceDTO pollingPlaceDTO : pollingPlaceDTOList1){
                            XWPFRun datosCausalsVotes = causalsuVotesSection.createRun();
                            datosCausalsVotes.setBold(false);
                            datosCausalsVotes.addCarriageReturn();
                            log.debug("POLLING PLACE {}", pollingPlaceDTO.toString());
                            datosCausalsVotes.setText("     - Sección: "+pollingPlaceDTO.getSection()+ ", casilla "+ this.getTipoCasilla(String.valueOf(pollingPlaceDTO.getTypePollingPlace()))  + " " +String.valueOf(pollingPlaceDTO.getTypeNumber()) );

                        }

                        j++;

                    }catch (NullPointerException e){
                        System.out.print("Caught the NullPointerException");
                    }
                }




            }






            XWPFParagraph paragraphThree = document.createParagraph();
            XWPFRun despedida = paragraphThree.createRun();
            despedida.addCarriageReturn();
            despedida.setText("Asimismo, en caso de que del resultado del cómputo distrital resultara que la diferencia porcentual entre el primer y segundo lugar fuera igual o menor a un punto porcentual, se solicita que ese consejo distrital realizar el recuento de votos en la totalidad de las casillas correspondientes a dicho Distrito Electoral "+ district.getRomanNumber()+" con cabecera en "+district.getDistrictHead()+".");
            despedida.addCarriageReturn();
            despedida.addCarriageReturn();
            despedida.setText("ATENTAMENTE");
            despedida.addCarriageReturn();
            despedida.addCarriageReturn();
            despedida.addCarriageReturn();
            despedida.setText(nameDemandant);
            despedida.addCarriageReturn();
            despedida.setText("Representante, "+ partyOrCoalitionOrCandidate );



            document.write(out);
            out.close();



        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }


    String getTipoCasilla(String casilla){
        if(casilla == "BASIC") return "Básica ";
        if(casilla == "CONTIGUOUS") return "Contigua ";
        if(casilla == "EXTRAORDINARY") return "Extraordinaria ";
        if(casilla == "SPECIAL") return "Especial ";

        return   casilla;
    }

}
