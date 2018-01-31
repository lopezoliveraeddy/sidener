(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionRecountDistrictController', ElectionRecountDistrictController);

    ElectionRecountDistrictController.$inject = ['$scope', '$state', '$stateParams', 'Election', 'ElectionRecountDistrict', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function ElectionRecountDistrictController($scope, $state, $stateParams, Election, ElectionRecountDistrict, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.lugares= [];
        vm.districts = [];
        // Datos de la Elección
        vm.loadElection = loadElection;

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
            ElectionRecountDistrict.get({ id : $stateParams.id
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

        $scope.supuestoRecuento = function(totalFirstPlace, totalSecondPlace, totalVotes) {
            var difference = ((totalFirstPlace - totalSecondPlace) / totalVotes);
            if(difference < .01) {
                return "total-recount";
            }
        };
    }
})();
