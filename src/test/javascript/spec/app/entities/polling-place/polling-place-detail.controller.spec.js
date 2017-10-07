'use strict';

describe('Controller Tests', function() {

    describe('PollingPlace Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPollingPlace, MockElection, MockDistrict;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPollingPlace = jasmine.createSpy('MockPollingPlace');
            MockElection = jasmine.createSpy('MockElection');
            MockDistrict = jasmine.createSpy('MockDistrict');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PollingPlace': MockPollingPlace,
                'Election': MockElection,
                'District': MockDistrict
            };
            createController = function() {
                $injector.get('$controller')("PollingPlaceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sidenerApp:pollingPlaceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
