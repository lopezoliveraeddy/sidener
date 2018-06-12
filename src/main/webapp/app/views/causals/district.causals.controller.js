(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionCausalsDistrictController', ElectionCausalsDistrictController);

    ElectionCausalsDistrictController.$inject = ['$state', '$stateParams', 'Election', 'ElectionCausalsDistrict', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','DistrictCausalsPollingPlaces','DistrictCausalsSearchPollingPlaces','DistrictDownload'];

    function ElectionCausalsDistrictController($state, $stateParams, Election, ElectionCausalsDistrict, ParseLinks, AlertService, paginationConstants, pagingParams,DistrictCausalsPollingPlaces,DistrictCausalsSearchPollingPlaces,DistrictDownload) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.demandByDistrict = demandByDistrict
        vm.getCausalDemand = getCausalDemand;


        // Datos de la Elección
        vm.loadElection = loadElection;
        vm.election = [];

        loadElection();
        loadAll();

        function demandByDistrict(id){
            DistrictCausalsPollingPlaces.get({id:id},onSuccess, onError);

            function onSuccess(data){
                angular.forEach(data,function(value,key){
                    DistrictCausalsSearchPollingPlaces.query({
                        query:"idPollingPlace:"+value.id
                    },fsuccess,ferror);

                    function fsuccess(data){
                        if(data.length > 0 ){
                        }
                    }
                    function ferror(error){
                        AlertService.error(error.data.message);
                    }
                });
            }

            function onError(error){
                AlertService.error(error.data.message);
            }
        }

        function loadElection () {
            Election.get({ id : $stateParams.idElection }, onSuccess, onError);
            function onSuccess(data) {
                vm.election = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll () {
            ElectionCausalsDistrict.get({
                idElection : $stateParams.idElection,
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate === 'decimalNumber') {
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


        function getCausalDemand(district) {
            console.log(district);
            DistrictDownload.get(district).then(function(response) {

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
                console.log(fileURL);

                a.href = fileURL;
                a.download = filename;
                a.click();
            }).catch(function(error) {
                AlertService.error(error);
            });
        }

    }
})();
