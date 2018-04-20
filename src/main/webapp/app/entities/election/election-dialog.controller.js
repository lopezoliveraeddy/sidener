(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionDialogController', ElectionDialogController);

    ElectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Election', 'ElectionType', 'PoliticalParty', 'Coalition', 'IndependentCandidate', 'User','Upload'];

    function ElectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Election, ElectionType, PoliticalParty, Coalition, IndependentCandidate, User) {
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
        vm.users = User.query();

        /*Files*/
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        ini();

        function ini() {
            if(vm.election.politicalPartyAssociatedId !== null) {
                $scope.checked = function() {
                    return 'politicalPartyAssociated';
                }
            }
            if(vm.election.coalitionAssociatedId !== null) {
                $scope.checked = function() {
                    return 'coalitionAssociated';
                }
            }
            if(vm.election.independentCandidateAssociatedId !== null) {
                $scope.checked = function() {
                    return 'independentCandidateAssociated';
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
            if(value === 'politicalPartyAssociated') {
                vm.election.coalitionAssociatedId = null;
                vm.election.coalitionAssociatedName = null;
                vm.election.independentCandidateAssociatedId = null;
                vm.election.independentCandidateAssociatedName = null;
            }
            else if(value === 'coalitionAssociated') {
                vm.election.politicalPartyAssociatedId = null;
                vm.election.politicalPartyAssociatedName = null;
                vm.election.independentCandidateAssociatedId = null;
                vm.election.independentCandidateAssociatedName = null;
            }
            else if(value === 'independentCandidateAssociated') {
                vm.election.politicalPartyAssociatedId = null;
                vm.election.politicalPartyAssociatedName = null;
                vm.election.coalitionAssociatedId = null;
                vm.election.coalitionAssociatedName = null;
            }
        }
    }
})();
