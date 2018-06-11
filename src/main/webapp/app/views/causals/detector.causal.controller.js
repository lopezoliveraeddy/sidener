(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DetectorCausalController', DetectorCausalController);

    DetectorCausalController.$inject = ['$scope', '$state', '$stateParams', 'AlertService', 'CausalType', 'DetectorCausals', 'DetectorCausalsPollingPlace', 'District', 'Election', 'DistrictCausalsPollingPlaces', 'ParseLinks', 'PollingPlace', 'paginationConstants', 'pagingParams'];

    function DetectorCausalController($scope, $state, $stateParams, AlertService, CausalType, DetectorCausals, DetectorCausalsPollingPlace, District, Election, DistrictCausalsPollingPlaces, ParseLinks, PollingPlace, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;

        vm.election = [];
        vm.pollingPlace=[];

        // Causales
        vm.causals = [];
        vm.causalsNullity = CausalType.get({
            typeCausal: 'NULLITY'
        });
        vm.causalId = '';
        vm.causalName = '';
        vm.causalColor = '';
        vm.detectorCausals = {};
        vm.stateDetectorCausals = '';


        loadAll();

        function loadAll () {
            PollingPlace.get({ id : $stateParams.id
            }, onSuccess, onError);

            function onSuccess(data, headers) {
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

        $scope.bindCausal = function(id, name, color) {
            // Detector de Causales
            DetectorCausalsPollingPlace.get({
                idPollingPlace: $stateParams.id,
                idCausal: id
            }, onSuccess, onError);
            vm.causalId = id;
            vm.causalColor = color;
            vm.causalName = name
        };

        function onSuccess(data, headers) {
            vm.detectorCausals = data;
            vm.stateDetectorCausals = true;
        }

        function onError(error) {
            vm.stateDetectorCausals = false;
            vm.detectorCausals = {};
        }

        $scope.updateDetectorCausals = function(detectorCausals) {
            detectorCausals.idDistrict = $stateParams.distrito;
            DetectorCausals.update(detectorCausals, onSaveSuccess, onSaveError);
        };

        $scope.createDetectorCausals = function(detectorCausals, causalId) {
            vm.detectorCausals.idCausal = causalId;
            vm.detectorCausals.idPollingPlace = $stateParams.id;
            vm.detectorCausals.idDistrict = $stateParams.distrito;
            console.log(vm.detectorCausals);
            DetectorCausals.save(JSON.stringify(vm.detectorCausals), onSaveSuccess, onSaveError);

        };

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:detectorCausalsUpdate', result);
            vm.isSaving = false;
            vm.stateDetectorCausals = true;
        }

        function onSaveError () {
            vm.isSaving = false;
        }





    }
})();
