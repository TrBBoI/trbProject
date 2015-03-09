#include <iostream>
#include <vector>

enum ItemId {
	itemA,
	itemB,
	itemC,
	itemD,
	itemE,
	itemF,
	itemG,
	itemH,
	itemJ,
	total
};

struct Item {
	ItemId id;
	int cost; 
	bool active;
};

struct Option {
	int cost;
	std::vector<ItemId> idlist;
};

Item items[] = {
	{ itemA, 100, false },
	{ itemB, 200, false },
	{ itemC, 250, false },
	{ itemD, 200, false },
	{ itemE, 400, false },
	{ itemF, 450, false },
	{ itemG, 150, false },
	{ itemH, 150, false },
	{ itemJ, 225, false }
};

int availableFunds = 2000;

void printItems() {
	std::cout << "Items: ";
	for( int i = 0; i < total; ++i ) {
		std::cout << "{id: " << items[i].id << " cost: " << items[i].cost << " active: " << items[i].active << "}, ";
	}
	std::cout << std::endl;
	std::cout << std::endl;
}

int getTotalCost() {
	int totalCost = 0;
	for ( int i = 0; i < total; ++i ) {
		if ( items[i].active == false ) continue;
		totalCost += items[i].cost;
	}
	return totalCost;
}

void calculateAndUnset() {
	std::cout << "Calculating...\n";
	std::vector<Option> options;
	Option *bestOption;
	int totalCost = getTotalCost();
	int minReq = totalCost - availableFunds;
	std::cout << "available funds: " << availableFunds << " total cost: " << totalCost << " minReq: " << minReq << "\n";

	for ( int i = 0; i < total; ++i ) {
		if ( items[i].active == false ) continue;
		Option option;
		option.cost = items[i].cost;
		option.idlist.push_back( items[i].id );
		options.push_back( option );
	}

	bestOption = &options[0];

	for ( auto iter = options.begin(); iter != options.end(); ++iter ) {
		Option &optionsI = *iter;
		std::cout << "l";
		if ( optionsI.cost > minReq ) continue;

		for ( int j = 0; j < total; ++j ) {
			if ( items[j].active == false ) continue;

			bool exist = false;
			int kSize = optionsI.idlist.size();
			for ( int k = 0; k < kSize; ++k ) {
				if ( optionsI.idlist[k] != items[j].id ) continue;
				exist = true;
				break;
			}

			if ( exist ) continue;

			Option option;
			option.cost = optionsI.cost + items[j].cost;
			option.idlist = optionsI.idlist;
			option.idlist.push_back( items[j].id );
			std::cout << "c";
			if ( option.cost < minReq ) {
				std::cout << "r";
				options.push_back( option );
			}
			else if ( !bestOption || bestOption->cost < minReq || bestOption->cost > option.cost ) {
					std::cout << "q";
					bestOption = &option;
			}
		}
	}

	for ( int i = 0; i < bestOption->idlist.size(); ++i ) {
		items[bestOption->idlist[i]].active = false;
	}

	std::cout << std::endl;
}

int main() {
	printItems();

	for( int i = 0; i < total; ++i ) {
		items[i].active = true;
	}

	printItems();

	calculateAndUnset();

	int totalCost = getTotalCost();
	int minReq = totalCost - availableFunds;
	std::cout << "available funds: " << availableFunds << " total cost: " << totalCost << " minReq: " << minReq << "\n\n";

	printItems();

	return 0;
}
