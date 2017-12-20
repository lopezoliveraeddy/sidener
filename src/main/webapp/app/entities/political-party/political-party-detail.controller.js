(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PoliticalPartyDetailController', PoliticalPartyDetailController);

    PoliticalPartyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PoliticalParty', 'Archive', 'PublicArchive'];

    function PoliticalPartyDetailController($scope, $rootScope, $stateParams, previousState, entity, PoliticalParty, Archive, PublicArchive) {
        var vm = this;

        vm.politicalParty = entity;
        vm.previousState = previousState.name;

        vm.image = [];

        var unsubscribe = $rootScope.$on('sidenerApp:politicalPartyUpdate', function(event, result) {
            vm.politicalParty = result;
        });
        $scope.$on('$destroy', unsubscribe);

        ini();

        function ini() {
            PublicArchive.get({ id: vm.politicalParty.imageId }, onSuccess, onError);
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
