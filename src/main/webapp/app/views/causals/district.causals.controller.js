(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionCausalsDistrictController', ElectionCausalsDistrictController);

    ElectionCausalsDistrictController.$inject = ['$state', '$stateParams', 'Election', 'ElectionCausalsDistrict', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','DistrictCausalsPollingPlaces','DistrictCausalsSearchPollingPlaces'];

    function ElectionCausalsDistrictController($state, $stateParams, Election, ElectionCausalsDistrict, ParseLinks, AlertService, paginationConstants, pagingParams,DistrictCausalsPollingPlaces,DistrictCausalsSearchPollingPlaces) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.demandByDistrict = demandByDistrict;


        // Datos de la Elección
        vm.loadElection = loadElection;
        vm.election = [];

        loadElection();
        loadAll();

        function demandByDistrict(id){
            DistrictCausalsPollingPlaces.get({id:id},onSuccess, onError);

            function onSuccess(data, headers){
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
            Election.get({ id : $stateParams.id }, onSuccess, onError);
            function onSuccess(data) {
                vm.election = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll () {
            ElectionCausalsDistrict.get({ id : $stateParams.id
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

    }
})();
