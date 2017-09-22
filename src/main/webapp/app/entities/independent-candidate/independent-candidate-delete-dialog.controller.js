(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('IndependentCandidateDeleteController',IndependentCandidateDeleteController);

    IndependentCandidateDeleteController.$inject = ['$uibModalInstance', 'entity', 'IndependentCandidate'];

    function IndependentCandidateDeleteController($uibModalInstance, entity, IndependentCandidate) {
        var vm = this;

        vm.independentCandidate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            IndependentCandidate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
