(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PoliticalPartyDialogController', PoliticalPartyDialogController);

    PoliticalPartyDialogController.$inject = ['$rootElement', '$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q',  'Archive', 'AuthServerProvider', 'entity', 'PublicArchive', 'PoliticalParty'];

    function PoliticalPartyDialogController ($rootElement, $timeout, $scope, $stateParams, $uibModalInstance, $q, Archive, AuthServerProvider, entity, PublicArchive, PoliticalParty) {
        var vm = this;

        vm.politicalParty = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        /* Upload Files */
        vm.uploadStart = uploadStart;
        vm.successUpload = successUpload;
        vm.removeFile = removeFile;
        vm.loading = false;
        vm.error = false;
        vm.completeUpload = completeUpload;
        vm.errorUpload = errorUpload;
        vm.flow = null;

        vm.promises = [];
        vm.image = [];
        vm.trash = [];

        var archiveTemporary = "TEMPORARY";
        var archivePermanent = "PERMANENT";

        ini();

        function ini() {
            if(vm.politicalParty.id == null) {
                vm.politicalParty.published = true;
            }
            if(vm.politicalParty.imageId != null) {
                vm.promises.push(makePromiseImagen(vm.politicalParty.imageId));
            }
        }

        function makePromiseImagen(imageId) {
            var deferred = $q.defer();
            PublicArchive.get({ id : imageId}).$promise.then(function(data) {
                vm.image.push(data);
                angular.forEach(vm.image, function(image, key) {
                    var arrayTmp = image.path.split("/files");
                    if(arrayTmp.length > 1) {
                        image.path = arrayTmp[arrayTmp.length - 1];
                    }
                });
                deferred.resolve(data);
            }).catch(function() {
                deferred.reject("error");
            });
            return deferred.promise;
        }


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if(vm.image.length > 0) {
                angular.forEach(vm.image, function (image, key) {
                    vm.politicalParty.imageId = image.id;
                    console.log(vm.politicalParty.imageId);
                });
            } else {
                vm.politicalParty.imageId = null;
            }
            if (vm.politicalParty.id !== null) {
                vm.politicalParty.updatedDate = new Date();
                PoliticalParty.update(vm.politicalParty, onSaveSuccess, onSaveError);
            } else {
                vm.politicalParty.createdDate = new Date();
                vm.politicalParty.updatedDate = new Date();
                PoliticalParty.save(vm.politicalParty, onSaveSuccess, onSaveError);
            }
        }



        // Saving Entity
        function onSaveSuccess (result) {
            console.log("OnSaveSuccess");
            $scope.$emit('sidenerApp:politicalPartyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;

            angular.forEach(vm.image, function(image, key) {
                if (image.id !== null) {
                    image.status = archivePermanent;
                    Archive.update(image, onSaveArchiveSuccess, onSaveArchiveError);
                }
            });
            angular.forEach(vm.trash, function(trash, key) {
                if (trash.id !== null) {
                    trash.status = archiveTemporary;
                    Archive.update(trash, onSaveArchiveSuccess, onSaveArchiveError);
                }
            });
        }
        // Error Entity
        function onSaveError () {
            vm.isSaving = false;
        }
        // Saving Archive
        function onSaveArchiveSuccess (result) {
        }
        // Error Archive
        function onSaveArchiveError () {
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
            vm.image.push(imagen);
            vm.imageId = imagen.id;
            angular.forEach(vm.image, function(value, key) {
                var arrayTmp = value.path.split("/files");
                if(arrayTmp.length > 1) {
                    value.path = arrayTmp[arrayTmp.length - 1];
                }
            });
            console.log("size total: " + $flow.sizeUploaded());
        }

        function removeFile(image) {
            console.log(image);
            vm.image.length = 0;
            vm.trash.push(image);
        }
    }
})();
