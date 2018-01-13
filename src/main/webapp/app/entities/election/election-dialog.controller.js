(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionDialogController', ElectionDialogController);

    ElectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Election', 'ElectionType', 'PoliticalParty', 'Coalition', 'IndependentCandidate', 'Causal', 'User','Upload'];

    function ElectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Election, ElectionType, PoliticalParty, Coalition, IndependentCandidate, Causal, User) {
        var vm = this;

        vm.election = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.electiontypes = ElectionType.query();
        vm.politicalparties = PoliticalParty.query();
        vm.coalitions = Coalition.query();
        vm.independentcandidates = IndependentCandidate.query();
        vm.causals = Causal.query();
        vm.users = User.query();
        /*Files*/
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        ini();

        function ini() {
            if(vm.election.politicalPartyAsociatedId !== null) {
                $scope.checked = function() {
                    return 'politicalPartyAsociated';
                }
            }
            if(vm.election.coalitionAsociatedId !== null) {
                $scope.checked = function() {
                    return 'coalitionAsociated';
                }
            }
            if(vm.election.independentCandidateAsociatedId !== null) {
                $scope.checked = function() {
                    return 'independentCandidateAsociated';
                }
            }
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.election.id !== null) {
                Election.update(vm.election, onSaveSuccess, onSaveError);
            } else {
                Election.save(vm.election, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:electionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateElection = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        
        /*files dataBase*/
        vm.setDatabase = function($file , election){
        	    if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                    	   election.dbFile = base64Data;
                        election.dbFileContentType = $file.type;
                    });
                });
            }
           
        };
        
        /*files Encarte*/
        vm.setEncarte = function ($file, election){
        	if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                    	     election.iuFile = base64Data;
                        election.iuFileContentType = $file.type;
                    });
                });
            }
        }
        /* files hojas de incidentes*/
        vm.setHojaIncidentes = function ($file, election){
	        	if ($file) {
	                DataUtils.toBase64($file, function(base64Data) {
	                    $scope.$apply(function() {
	                    	    election.isFile = base64Data;
	                        election.isFileContentType = $file.type;
	                    });
	                });
	            }
	        }      	
        /* files acta de la jornada*/
        vm.setActaJornada = function ($file, election){
        	if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                    	    election.drFile = base64Data;
                        election.drFileContentType = $file.type;
                    });
                });
            }
        }   
        /* plantilla de la demanda */
        vm.setPlantillaDemanda = function ($file, election){
        	if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                    	    election.dmFile = base64Data;
                        election.dmFileContentType = $file.type;
                    });
                });
            }
        }   
        /*plantilla de recuento*/
        vm.setPlantillaRecuento = function ($file, election){
        	if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                    	    election.rtFile = base64Data;
                        election.rtFileContentType = $file.type;
                    });
                });
            }
        }   
        
        $scope.change = function(value) {
            console.log(value);
            if(value === 'politicalPartyAsociated') {
                vm.election.coalitionAsociatedId = null;
                vm.election.coalitionAsociatedName = null;
                vm.election.independentCandidateAsociatedId = null;
                vm.election.independentCandidateAsociatedName = null;
            }
            else if(value === 'coalitionAsociated') {
                vm.election.politicalPartyAsociatedId = null;
                vm.election.politicalPartyAsociatedName = null;
                vm.election.independentCandidateAsociatedId = null;
                vm.election.independentCandidateAsociatedName = null;
            }
            else if(value === 'independentCandidateAsociated') {
                vm.election.politicalPartyAsociatedId = null;
                vm.election.politicalPartyAsociatedName = null;
                vm.election.coalitionAsociatedId = null;
                vm.election.coalitionAsociatedName = null;
            }
        }
    }
})();
