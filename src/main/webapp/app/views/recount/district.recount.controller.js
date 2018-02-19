(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionRecountDistrictController', ElectionRecountDistrictController);

    ElectionRecountDistrictController.$inject = ['$scope', '$state', '$stateParams', 'Election', 'ElectionRecountDistrict', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','DocumentDownload'];

    function ElectionRecountDistrictController($scope, $state, $stateParams, Election, ElectionRecountDistrict, ParseLinks, AlertService, paginationConstants, pagingParams,DocumentDownload) {

        var vm = this;
        console.log("eddy");
        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.lugares= [];
        vm.districts = [];
        // Datos de la Elección
        vm.loadElection = loadElection;
        vm.openLink = openLink;

        loadElection();
        loadAll();

        function loadElection () {
            Election.get({ id : $stateParams.id }, onSuccess, onError);
            function onSuccess(data) {
                vm.election = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll () {
            ElectionRecountDistrict.get({ idElection : $stateParams.id
            }, onSuccess, onError);

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
                id: $stateParams.id,
                page: vm.page
            });
        }

        function openLink(file){
            console.log(file);
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
    }
})();
