(function() {
    'use strict';

    angular
        .module('newApp')
        .controller('AddressDetailController', AddressDetailController);

    AddressDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Address', 'Employee'];

    function AddressDetailController($scope, $rootScope, $stateParams, previousState, entity, Address, Employee) {
        var vm = this;

        vm.address = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('newApp:addressUpdate', function(event, result) {
            vm.address = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
