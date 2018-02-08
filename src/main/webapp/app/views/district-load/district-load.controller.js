(function() {
	'use strict';

	angular
	    .module('sidenerApp')
	    .controller('DistrictLoadController', DistrictLoadController);

    DistrictLoadController.$inject = ['$timeout', '$scope', '$stateParams','DataUtils', 'ParseLinks', 'AlertService', 'paginationConstants','Election','LoadDistrict'];
	function DistrictLoadController ($timeout, $scope, $stateParams,DataUtils,ParseLinks, AlertService, paginationConstants,Election,LoadDistrict) {
		var vm = this;
        vm.loadAll = loadAll;
        vm.elections = null;
        vm.election = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.save = save;


		console.log("Carga de distritos");

        loadAll();

        function loadAll () {
            Election.query({}, onSuccess, onError);
        }

        function onSuccess(data, headers) {
            vm.totalItems = headers('X-Total-Count');
            vm.elections = data;
            console.log(vm.elections);
        }


        function save () {
            if( angular.isDefined(vm.electionSel) ){
                vm.bandera = 1;
                console.log("eddy");
                LoadDistrict.save(
                    {
                        "dbFile": vm.election.dbFile,
                        "dbFileContentType": vm.election.dbFileContentType,
                        "eleccion" :vm.electionSel.id
                    }
                );

            }


        }


        function onError(error) {
            AlertService.error(error.data.message);
        }


        vm.loadFile = function($file , election){

            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        election.dbFile = base64Data;
                        election.dbFileContentType = $file.type;

                    });
                });
            }
        };

	}


})();
