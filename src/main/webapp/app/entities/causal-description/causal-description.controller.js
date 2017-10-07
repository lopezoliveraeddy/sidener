(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CausalDescriptionController', CausalDescriptionController);

    CausalDescriptionController.$inject = ['DataUtils', 'CausalDescription', 'CausalDescriptionSearch'];

    function CausalDescriptionController(DataUtils, CausalDescription, CausalDescriptionSearch) {

        var vm = this;

        vm.causalDescriptions = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            CausalDescription.query(function(result) {
                vm.causalDescriptions = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CausalDescriptionSearch.query({query: vm.searchQuery}, function(result) {
                vm.causalDescriptions = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
