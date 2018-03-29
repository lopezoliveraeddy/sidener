(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DetectorCausalsDialogController', DetectorCausalsDialogController);

    DetectorCausalsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'DetectorCausals'];

    function DetectorCausalsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, DetectorCausals) {
        var vm = this;

        vm.detectorCausals = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.detectorCausals.id !== null) {
                DetectorCausals.update(vm.detectorCausals, onSaveSuccess, onSaveError);
            } else {
                DetectorCausals.save(vm.detectorCausals, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:detectorCausalsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
