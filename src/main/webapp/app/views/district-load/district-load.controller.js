(function() {
	'use strict';

	angular
	    .module('sidenerApp')
	    .controller('DistrictLoadController', DistrictLoadController);

    DistrictLoadController.$inject = ['$timeout', '$scope', '$state', '$stateParams','DataUtils', 'ParseLinks', 'AlertService', 'paginationConstants','Election','LoadDistrict','PollingPlace','DistrictRecountPollingPlaces','pagingParams','GenerateDistrict'];
	function DistrictLoadController ($timeout, $scope, $state, $stateParams,DataUtils,ParseLinks, AlertService, paginationConstants,Election,LoadDistrict,PollingPlace,DistrictRecountPollingPlaces,pagingParams,GenerateDistrict) {
		var vm = this;
        vm.loadAll = loadAll;
        vm.elections = null;
        vm.election = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.save = save;
        vm.predicate = pagingParams.predicate;
        vm.itemsPerPage = paginationConstants.itemsPerPage;



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

        vm.processDistrict = function(idDistrito){
            /*Obtener las casillas por Id*/
            console.log(idDistrito);
            GenerateDistrict.get({
                id: idDistrito,
                page: 1
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                console.log("eddytoWasHere");
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.pollingPlaces = data;
                vm.page = 1;
                console.log(vm.pollingPlaces);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };


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
