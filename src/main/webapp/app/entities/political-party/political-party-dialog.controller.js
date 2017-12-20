(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PoliticalPartyDialogController', PoliticalPartyDialogController);

    PoliticalPartyDialogController.$inject = ['$rootElement', '$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'AuthServerProvider', 'entity', 'Archive', 'PoliticalParty'];

    function PoliticalPartyDialogController ($rootElement, $timeout, $scope, $stateParams, $uibModalInstance, $q, AuthServerProvider, entity, Archive, PoliticalParty) {
        var vm = this;

        vm.politicalParty = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        /* Upload Files */
        vm.uploadStart = uploadStart;
        vm.successUpload = successUpload;
        vm.removeFiles = removeFiles;
        vm.loading = false;
        vm.error = false;
        vm.completeUpload = completeUpload;
        vm.errorUpload = errorUpload;
        vm.flow = null;
        vm.deleteFile = deleteFile;
        vm.filesToRemove = [];

        vm.imageId = "";

        vm.images = Archive.query({filter: 'politicalparty-is-null'});

        $q.all([vm.politicalParty.$promise, vm.images.$promise]).then(function() {
            if (!vm.politicalParty.imageId) {
                return $q.reject();
            }
            return Archive.get({id : vm.politicalParty.imageId}).$promise;
        }).then(function(image) {
            vm.images.push(image);
        });

        ini();

        function ini() {
            if(vm.politicalParty.id == null) {
                vm.politicalParty.published = true;
            }
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            vm.politicalParty.imageId = vm.imageId;
            if (vm.politicalParty.id !== null) {
                vm.politicalParty.updatedDate = new Date();
                PoliticalParty.update(vm.politicalParty, onSaveSuccess, onSaveError);
            } else {
                vm.politicalParty.createdDate = new Date();
                vm.politicalParty.updatedDate = new Date();
                PoliticalParty.save(vm.politicalParty, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            console.log("OnSaveSuccess");
            $scope.$emit('sidenerApp:politicalPartyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        /* Upload Files */
        function uploadStart($flow) {
            console.log("uploadStart");
            vm.loading = true;
            var token = AuthServerProvider.getToken();
            if (token) {
                $flow.opts.headers.Authorization = 'Bearer ' + token;
                var directoryParam = $rootElement.attr('ng-app').toLowerCase().replace('app','');
                $flow.opts.headers.directory = directoryParam;
            }
            vm.error = false;
        }

        function errorUpload($file, $message, $flow) {
            console.log("ErrorUpload");
            vm.isSaving = false;
            $flow.removeFile($file);
            vm.error = true;
            var data = angular.fromJson($message);
            vm.error_message = data.error + ". Archivo:" + $file.name + ". Intente cargar nuevamente.";
            vm.loading = false;
        }

        function completeUpload($flow) {
            console.log("CompleteUpload");
            vm.flow = $flow;
            vm.loading = false;
        }

        function successUpload($file, $message, $flow) {
            console.log("SuccessUpload");
            var imagen = angular.fromJson($message);
            vm.imageId = imagen.id;
            console.log("size total: " + $flow.sizeUploaded());
        }

        function removeFiles($flow, index) {
            $flow.removeFile($flow.files[index]);
        }

        function deleteFile(file) {
            vm.filesToRemove.push(file);
            for (var i = 0; i < vm.documentosResponse.length; i++) {
                if (vm.documentosResponse[i].id == file.id) {
                    vm.documentosResponse.splice(i, 1);
                    break;
                }
            }
        }

        function removeLoadedFiles() {
            for (var i = 0; i < vm.filesToRemove.length; i++) {
                console.log(vm.filesToRemove[i].nombre);
                Documento.delete({
                    id: vm.filesToRemove[i].id
                }, onDeleteFileSuccess, onDeleteFileError);
            };
        }

        function onDeleteFileSuccess() {
            console.log("onDeleteFileSuccess");
        }

        function onDeleteFileError() {
            console.log("onDeleteFileError");
        }


    }
})();
