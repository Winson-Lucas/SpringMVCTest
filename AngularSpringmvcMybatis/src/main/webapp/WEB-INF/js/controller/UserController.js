
/**
 * UserController
 */
var UserController = function($scope, $http) {
    $scope.fetchUsersList = function() {
        $http.get('users/userlist.json').success(function(userList){
            $scope.users = userList;
        });
    };

    $scope.addNewUser = function(newUser) {
        $http.post('users/addUser/' + newUser).success(function() {
            $scope.fetchUsersList();
        });
        $scope.userName = '';
    };

    $scope.removeUser = function(user) {
    };

    $scope.removeAllUsers = function() {

    };

    $scope.fetchUsersList();
};