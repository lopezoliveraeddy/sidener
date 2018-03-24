'use strict';

describe('Controller Tests', function() {

    describe('Pollingplace Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPollingplace, MockArchive, MockElection, MockDistrict, MockCausal;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPollingplace = jasmine.createSpy('MockPollingplace');
            MockArchive = jasmine.createSpy('MockArchive');
            MockElection = jasmine.createSpy('MockElection');
            MockDistrict = jasmine.createSpy('MockDistrict');
            MockCausal = jasmine.createSpy('MockCausal');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Pollingplace': MockPollingplace,
                'Archive': MockArchive,
                'Election': MockElection,
                'District': MockDistrict,
                'Causal': MockCausal
            };
            createController = function() {
                $injector.get('$controller')("PollingplaceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'sidenerApp:pollingplaceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
