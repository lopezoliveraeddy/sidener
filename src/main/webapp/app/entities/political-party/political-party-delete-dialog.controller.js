(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PoliticalPartyDeleteController',PoliticalPartyDeleteController);

    PoliticalPartyDeleteController.$inject = ['$uibModalInstance', 'entity', 'PoliticalParty'];

    function PoliticalPartyDeleteController($uibModalInstance, entity, PoliticalParty) {
        var vm = this;

        vm.politicalParty = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PoliticalParty.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
