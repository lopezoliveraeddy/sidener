'use strict';

describe('Controller Tests', function() {

    describe('PollingPlace Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPollingPlace, MockArchive, MockElection, MockDistrict, MockCausal;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPollingPlace = jasmine.createSpy('MockPollingPlace');
            MockArchive = jasmine.createSpy('MockArchive');
            MockElection = jasmine.createSpy('MockElection');
            MockDistrict = jasmine.createSpy('MockDistrict');
            MockCausal = jasmine.createSpy('MockCausal');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PollingPlace': MockPollingPlace,
                'Archive': MockArchive,
                'Election': MockElection,
                'District': MockDistrict,
                'Causal': MockCausal
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
