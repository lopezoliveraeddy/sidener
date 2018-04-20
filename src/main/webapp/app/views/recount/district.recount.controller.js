(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionRecountDistrictController', ElectionRecountDistrictController);

    ElectionRecountDistrictController.$inject = ['$scope', '$state', '$stateParams', 'Election', 'ElectionDistrictsWonLose','ElectionDistrictsRecount', 'ElectionRecountDistrict', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','DocumentDownload','DemandDownload'];

    function ElectionRecountDistrictController($scope, $state, $stateParams, Election, ElectionDistrictsWonLose, ElectionDistrictsRecount, ElectionRecountDistrict, ParseLinks, AlertService, paginationConstants, pagingParams, DocumentDownload, DemandDownload) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.lugares= [];
        vm.districts = [];
        // Datos de la Elección
        vm.loadElection = loadElection;
        vm.openLink = openLink;
        // Distritos Ganados - Perdidos
        vm.districtsWonLose = ElectionDistrictsWonLose.get({ idElection : $stateParams.idElection });
        vm.districtsForTotalRecount = '';
        vm.trulyEnabledDIstricts = [];
        vm.districtsForDemand = '';

        loadElection();
        loadDistrictsRecountTotal();
        loadAll();


        function loadElection () {
            Election.get({ id : $stateParams.idElection }, onSuccess, onError);
            function onSuccess(data) {
                vm.election = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadDistrictsRecountTotal() {
            ElectionDistrictsRecount.get({ idElection : $stateParams.idElection }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.districtsForTotalRecount = headers('X-Total-Count');
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll() {
            ElectionRecountDistrict.get({
                idElection : $stateParams.idElection,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate === 'districtWon') {
                    result.push('decimalNumber');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.districts = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                idElection: $stateParams.idElection,
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')
            });
        }

        function openLink(file){
            DocumentDownload.get(file).then(function(response) {

                var contentDisposition = response.headers("content-disposition");
                var tmp = contentDisposition.split("filename=");
                var filename = "";

                if(tmp.length>1){
                    filename = tmp[1].replace(/\"/g, '');
                }

                var a = document.createElement("a");
                document.body.appendChild(a);
                var file = new Blob([response.data], {type: 'application/octet-stream'});
                var fileURL = URL.createObjectURL(file);
                a.href = fileURL;
                a.download = filename;
                a.click();
            }).catch(function(error) {
                AlertService.error(error);
            });
        }

        $scope.countingAssumption = function(countingAssumption) {
            if (countingAssumption === true) {
                return "total-recount";
            }
        };

        $scope.associatedPosition = function(entity) {
            if (vm.election.coalitionAssociatedId !== null) {
                if (entity === vm.election.coalitionAssociatedAcronym) {
                    return '<i class="fa medium fa-certificate" aria-hidden="true"></i>';
                }
            }
            else if(vm.election.politicalPartyAssociatedId !== null) {
                if (entity === vm.election.politicalPartyAssociatedAcronym) {
                    return '<i class="fa medium fa-certificate" aria-hidden="true"></i>';
                }
            }
            else if (vm.election.independentCandidateAssociatedId !== null) {
                if (entity === vm.election.independentCandidateAssociatedAcronym) {
                    return '<i class="fa medium fa-certificate" aria-hidden="true"></i>';
                }
            }
        }
    }
})();
