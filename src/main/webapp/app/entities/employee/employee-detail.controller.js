(function() {
    'use strict';

    angular
        .module('newApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employee', 'Address'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, previousState, entity, Employee, Address) {
        var vm = this;

        vm.employee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('newApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
