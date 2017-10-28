'use strict';

describe('Controller Tests', function() {

    describe('Election Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockElection, MockElectionType, MockPoliticalParty, MockCoalition, MockIndependentCandidate, MockCausal, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockElection = jasmine.createSpy('MockElection');
            MockElectionType = jasmine.createSpy('MockElectionType');
            MockPoliticalParty = jasmine.createSpy('MockPoliticalParty');
            MockCoalition = jasmine.createSpy('MockCoalition');
            MockIndependentCandidate = jasmine.createSpy('MockIndependentCandidate');
            MockCausal = jasmine.createSpy('MockCausal');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Election': MockElection,
                'ElectionType': MockElectionType,
                'PoliticalParty': MockPoliticalParty,
                'Coalition': MockCoalition,
                'IndependentCandidate': MockIndependentCandidate,
                'Causal': MockCausal,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ElectionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sidenerApp:electionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
