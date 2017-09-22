'use strict';

describe('Controller Tests', function() {

    describe('ElectionPeriod Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockElectionPeriod;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockElectionPeriod = jasmine.createSpy('MockElectionPeriod');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ElectionPeriod': MockElectionPeriod
            };
            createController = function() {
                $injector.get('$controller')("ElectionPeriodDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sidenerApp:electionPeriodUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
