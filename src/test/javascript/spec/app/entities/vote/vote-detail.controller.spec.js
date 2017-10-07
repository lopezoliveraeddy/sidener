'use strict';

describe('Controller Tests', function() {

    describe('Vote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVote, MockElection, MockPoliticalParty, MockIndependentCandidate, MockCoalition, MockPollingPlace;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVote = jasmine.createSpy('MockVote');
            MockElection = jasmine.createSpy('MockElection');
            MockPoliticalParty = jasmine.createSpy('MockPoliticalParty');
            MockIndependentCandidate = jasmine.createSpy('MockIndependentCandidate');
            MockCoalition = jasmine.createSpy('MockCoalition');
            MockPollingPlace = jasmine.createSpy('MockPollingPlace');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Vote': MockVote,
                'Election': MockElection,
                'PoliticalParty': MockPoliticalParty,
                'IndependentCandidate': MockIndependentCandidate,
                'Coalition': MockCoalition,
                'PollingPlace': MockPollingPlace
            };
            createController = function() {
                $injector.get('$controller')("VoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sidenerApp:voteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
