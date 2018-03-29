(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DetectorCausalController', DetectorCausalController);

    DetectorCausalController.$inject = ['$scope', '$state', '$stateParams', 'AlertService', 'CausalType', 'District', 'Election', 'DistrictCausalsPollingPlaces', 'ParseLinks', 'PollingPlace', 'paginationConstants', 'pagingParams'];

    function DetectorCausalController($scope, $state, $stateParams, AlertService, CausalType, District, Election, DistrictCausalsPollingPlaces, ParseLinks, PollingPlace, paginationConstants, pagingParams) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;

        vm.election = [];
        vm.pollingPlace=[];

        vm.texto= '';
        vm.direccion= '';
        vm.causalA= false;
        vm.causalB= false;
        vm.causalC= false;
        vm.causalD= false;
        vm.causalE= false;

        // Causales
        vm.causals = [];
        vm.causalsNullity = CausalType.get({
            typeCausal: 'NULLITY'
        });
        vm.causalName = '';
        vm.causalColor = '';

        loadAll();
        vm.lugar = false;

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

        $scope.bindCausal = function(color, name) {
            vm.causalColor = color;
            vm.causalName = name
        };
    }
})();
