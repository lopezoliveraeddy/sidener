(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DetectorCausalsController', DetectorCausalsController);

    DetectorCausalsController.$inject = ['DataUtils', 'DetectorCausals', 'DetectorCausalsSearch'];

    function DetectorCausalsController(DataUtils, DetectorCausals, DetectorCausalsSearch) {

        var vm = this;

        vm.detectorCausals = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            DetectorCausals.query(function(result) {
                vm.detectorCausals = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            DetectorCausalsSearch.query({query: vm.searchQuery}, function(result) {
                vm.detectorCausals = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
