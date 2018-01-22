(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PollingPlaceDialogController', PollingPlaceDialogController);

    PollingPlaceDialogController.$inject = ['$rootElement', '$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'AuthServerProvider', 'DataUtils', 'entity', 'PollingPlace', 'Archive', 'Election', 'District', 'Causal', 'PublicArchive'];

    function PollingPlaceDialogController ($rootElement, $timeout, $scope, $stateParams, $uibModalInstance, $q, AuthServerProvider, DataUtils, entity, PollingPlace, Archive, Election, District, Causal, PublicArchive) {
        var vm = this;

        vm.pollingPlace = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.elections = Election.query();
        vm.districts = District.query();
        vm.causals = Causal.query();

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
            if(vm.pollingPlace.id == null) {
                vm.pollingPlace.published = true;
            }
            if(vm.pollingPlace.recordCountId != null) {
                vm.promises.push(makePromiseImage(vm.pollingPlace.recordCountId));
            }
        }

        function makePromiseImage(imageId) {
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
                    vm.pollingPlace.recordCountId = image.id;
                });
            } else {
                vm.pollingPlace.recordCountId = null;
            }
            if (vm.pollingPlace.id !== null) {
                vm.pollingPlace.updatedDate = new Date();
                PollingPlace.update(vm.pollingPlace, onSaveSuccess, onSaveError);
            } else {
                vm.pollingPlace.createdDate = new Date();
                vm.pollingPlace.updatedDate = new Date();
                PollingPlace.save(vm.pollingPlace, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:pollingPlaceUpdate', result);
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
