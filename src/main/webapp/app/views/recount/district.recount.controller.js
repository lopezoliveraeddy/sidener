(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionRecountDistrictController', ElectionRecountDistrictController);

    ElectionRecountDistrictController.$inject = ['$state', '$stateParams', 'Election', 'ElectionRecountDistrict', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function ElectionRecountDistrictController($state, $stateParams, Election, ElectionRecountDistrict, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;
        vm.lugares= [];

        // Datos de la Elección
        vm.loadElection = loadElection;
        vm.lugares = [
            ["MORENA",509,"PAN",555,1208],
            ["PRI",149,"PRD",942,2252],
            ["PRI",105,"MORENA",407,1349],
            ["PAN",746,"PT",1194,3565],
            ["PRI",733,"MC",789,2038],
            ["PAN",172,"PANAL",412,1161],
            ["PRD",526,"PAN",1702,6157],
            ["MORENA",200,"PRD",433,647],
            ["PRD",323,"MORENA",778,3007],
            ["MORENA",457,"PT",757,2067],
            ["PRI",711,"MC",1760,5229],
            ["PAN",505,"PANAL",1396,5475],
            ["PRD",304,"PAN",1229,2572],
            ["PRI",234,"PRD",353,828],
            ["PAN",728,"MORENA",772,2562],
            ["PRI",167,"PT",495,1354],
            ["PAN",179,"MC",1025,3662],
            ["PRD",210,"PANAL",1655,2431],
            ["MORENA",125,"PAN",508,1139],
            ["PRD",475,"PRD",874,2725],
            ["MORENA",759,"PT",859,1798],
            ["PRI",668,"PT",1701,5165],
        ];

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



    }
})();
