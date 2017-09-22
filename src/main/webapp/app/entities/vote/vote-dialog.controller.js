(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('VoteDialogController', VoteDialogController);

    VoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Vote', 'PoliticalParty', 'IndependentCandidate', 'Coalition', 'PollingPlace'];

    function VoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Vote, PoliticalParty, IndependentCandidate, Coalition, PollingPlace) {
        var vm = this;

        vm.vote = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.politicalparties = PoliticalParty.query({filter: 'vote-is-null'});
        $q.all([vm.vote.$promise, vm.politicalparties.$promise]).then(function() {
            if (!vm.vote.politicalPartyId) {
                return $q.reject();
            }
            return PoliticalParty.get({id : vm.vote.politicalPartyId}).$promise;
        }).then(function(politicalParty) {
            vm.politicalparties.push(politicalParty);
        });
        vm.independentcandidates = IndependentCandidate.query({filter: 'vote-is-null'});
        $q.all([vm.vote.$promise, vm.independentcandidates.$promise]).then(function() {
            if (!vm.vote.independentCandidateId) {
                return $q.reject();
            }
            return IndependentCandidate.get({id : vm.vote.independentCandidateId}).$promise;
        }).then(function(independentCandidate) {
            vm.independentcandidates.push(independentCandidate);
        });
        vm.coalitions = Coalition.query({filter: 'vote-is-null'});
        $q.all([vm.vote.$promise, vm.coalitions.$promise]).then(function() {
            if (!vm.vote.coalitionId) {
                return $q.reject();
            }
            return Coalition.get({id : vm.vote.coalitionId}).$promise;
        }).then(function(coalition) {
            vm.coalitions.push(coalition);
        });
        vm.pollingplaces = PollingPlace.query({filter: 'vote-is-null'});
        $q.all([vm.vote.$promise, vm.pollingplaces.$promise]).then(function() {
            if (!vm.vote.pollingPlaceId) {
                return $q.reject();
            }
            return PollingPlace.get({id : vm.vote.pollingPlaceId}).$promise;
        }).then(function(pollingPlace) {
            vm.pollingplaces.push(pollingPlace);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vote.id !== null) {
                Vote.update(vm.vote, onSaveSuccess, onSaveError);
            } else {
                Vote.save(vm.vote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:voteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.created = false;
        vm.datePickerOpenStatus.updated = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
