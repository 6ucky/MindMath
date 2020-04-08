equal(X, Y) :-
	X = Y.

strictinf(X, Y) :-
	X < Y.

inf(X, Y) :-
	strictinf(X, Y);
	equal(X, Y).

strictsup(X, Y) :-
	strictinf(Y, X).

sup(X, Y) :-
	strictsup(X, Y);
	equal(X, Y).

strictbetween(X, Y, Z) :-
	strictsup(X, Y),
	strictinf(X, Z).

between(X, Y, Z) :-
	strictbetween(X, Y, Z);
	equal(X, Y);
	equal(X, Z).
