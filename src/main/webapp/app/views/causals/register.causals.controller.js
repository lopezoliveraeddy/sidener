(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('RegisterCausalsDistrictController', RegisterCausalsDistrictController);

    RegisterCausalsDistrictController.$inject = ['$state', '$stateParams', 'Election', 'ElectionCausalsDistrict', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','PollingPlace'];

    function RegisterCausalsDistrictController($state, $stateParams, Election, ElectionCausalsDistrict, ParseLinks, AlertService, paginationConstants, pagingParams,PollingPlace) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;

        vm.election = [];
        vm.pollingPlace=[];

        loadAll();

        vm.men = [
            'John',
            'Jack',
            'Mark',
            'Ernie'
        ];


        vm.women = [
            'Jane',
            'Jill',
            'Betty',
            'Mary'
        ];




        vm.dropSuccessHandler = function($event,index,array){
            array.splice(index,1);
        };

        vm.onDrop = function($event,$data,array){
            array.push($data);
        };




        function loadAll () {
            PollingPlace.get({ id : $stateParams.id
                }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.pollingPlace = data;
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
