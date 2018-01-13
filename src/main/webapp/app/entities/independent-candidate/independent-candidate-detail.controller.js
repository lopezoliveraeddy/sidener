(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('IndependentCandidateDetailController', IndependentCandidateDetailController);

    IndependentCandidateDetailController.$inject = ['$scope', '$rootScope', 'previousState', 'entity', 'PublicArchive'];

    function IndependentCandidateDetailController($scope, $rootScope, previousState, entity, PublicArchive) {
        var vm = this;

        vm.independentCandidate = entity;
        vm.previousState = previousState.name;

        vm.image = [];

        var unsubscribe = $rootScope.$on('sidenerApp:independentCandidateUpdate', function(event, result) {
            vm.independentCandidate = result;
        });
        $scope.$on('$destroy', unsubscribe);

        ini();

        function ini() {
            if(vm.independentCandidate.imageId !== null) {
                PublicArchive.get({id: vm.independentCandidate.imageId}, onSuccess, onError);
            }
        }
        function onSuccess(data, headers) {
            vm.image = data;
            var arrayTmp = vm.image.path.split("/files");
            if(arrayTmp.length > 1) {
                vm.image.path = arrayTmp[arrayTmp.length - 1];
            }
        }
        function onError() {

        }
    }
})();
