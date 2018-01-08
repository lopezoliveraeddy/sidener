'use strict';

describe('Controller Tests', function() {

    describe('Coalition Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCoalition, MockArchive, MockPoliticalParty;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCoalition = jasmine.createSpy('MockCoalition');
            MockArchive = jasmine.createSpy('MockArchive');
            MockPoliticalParty = jasmine.createSpy('MockPoliticalParty');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Coalition': MockCoalition,
                'Archive': MockArchive,
                'PoliticalParty': MockPoliticalParty
            };
            createController = function() {
                $injector.get('$controller')("CoalitionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sidenerApp:coalitionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
