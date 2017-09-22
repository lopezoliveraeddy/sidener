'use strict';

describe('Controller Tests', function() {

    describe('Election Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockElection, MockState, MockElectionType, MockElectionPeriod, MockPoliticalParty, MockIndependentCandidate, MockCoalition, MockCausal;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockElection = jasmine.createSpy('MockElection');
            MockState = jasmine.createSpy('MockState');
            MockElectionType = jasmine.createSpy('MockElectionType');
            MockElectionPeriod = jasmine.createSpy('MockElectionPeriod');
            MockPoliticalParty = jasmine.createSpy('MockPoliticalParty');
            MockIndependentCandidate = jasmine.createSpy('MockIndependentCandidate');
            MockCoalition = jasmine.createSpy('MockCoalition');
            MockCausal = jasmine.createSpy('MockCausal');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Election': MockElection,
                'State': MockState,
                'ElectionType': MockElectionType,
                'ElectionPeriod': MockElectionPeriod,
                'PoliticalParty': MockPoliticalParty,
                'IndependentCandidate': MockIndependentCandidate,
                'Coalition': MockCoalition,
                'Causal': MockCausal
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
