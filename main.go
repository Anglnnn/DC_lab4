package main

import (
	"fmt"
	"sync"
	"time"
)

type Graph struct {
	mutex  sync.RWMutex
	cities map[string]City
}

type City struct {
	name   string
	routes map[string]Route
}

type Route struct {
	destination string
	price       int
}

func (g *Graph) AddCity(city City) {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	g.cities[city.name] = city
}

func (g *Graph) RemoveCity(cityName string) {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	delete(g.cities, cityName)
}

func (g *Graph) AddRoute(source, destination string, price int) {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	city := g.cities[source]
	city.routes[destination] = Route{destination, price}

	city = g.cities[destination]
	city.routes[source] = Route{source, price}
}

func (g *Graph) RemoveRoute(source, destination string) {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	delete(g.cities[source].routes, destination)
	delete(g.cities[destination].routes, source)
}

func (g *Graph) ChangeTicketPrice(route string, newPrice int) {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	city1, city2 := route[0:1], route[2:]

	var route1, route2 Route

	route1 = g.cities[city1].routes[city2]
	route2 = g.cities[city2].routes[city1]

	route1.price = newPrice
	route2.price = newPrice

	g.cities[city1].routes[city2] = route1
	g.cities[city2].routes[city1] = route2
}

func (g *Graph) HasPath(source, destination string) bool {
	g.mutex.RLock()
	defer g.mutex.RUnlock()

	visited := make(map[string]bool)
	queue := []string{source}

	for len(queue) > 0 {
		city := queue[0]
		queue = queue[1:]

		if city == destination {
			return true
		}

		visited[city] = true

		for neighbor := range g.cities[city].routes {
			if !visited[neighbor] {
				queue = append(queue, neighbor)
			}
		}
	}

	return false
}

func (g *Graph) GetPathPrice(source, destination string) int {
	g.mutex.RLock()
	defer g.mutex.RUnlock()

	queue := []string{source}
	cost := 0

	for len(queue) > 0 {
		city := queue[0]
		queue = queue[1:]

		if city == destination {
			return cost
		}

		route := Route{}

		for neighbor, route := range g.cities[city].routes {
			if neighbor == destination {
				return cost + route.price
			}

			queue = append(queue, neighbor)
		}

		cost += route.price
	}

	return -1
}

func main() {
	graph := Graph{}

	graph.cities = make(map[string]City)

	city1 := City{"A", make(map[string]Route)}
	city2 := City{"B", make(map[string]Route)}
	city3 := City{"C", make(map[string]Route)}

	graph.AddCity(city1)
	graph.AddCity(city2)
	graph.AddCity(city3)

	graph.AddRoute("A", "B", 100)
	graph.AddRoute("B", "A", 100)
	graph.AddRoute("A", "C", 50)
	graph.AddRoute("C", "A", 50)
	graph.AddRoute("B", "C", 20)
	graph.AddRoute("C", "B", 20)

	go func() {
		for {

			graph.ChangeTicketPrice("A-B", 100)
			time.Sleep(time.Second)
		}
	}()

	go func() {
		for {


			graph.mutex.Lock()
			graph.RemoveCity("C")
			graph.mutex.Unlock()

			graph.mutex.Lock()
			graph.AddRoute("A", "C", 50)
			graph.mutex.Unlock()

			time.Sleep(time.Second)

			graph.mutex.Lock()
			graph.AddCity(City{"C", make(map[string]Route)})
			graph.mutex.Unlock()

			time.Sleep(time.Second)
		}
	}()

	go func() {
		for {
			graph.AddCity(City{name: "D"})
			time.Sleep(time.Second)

			graph.RemoveCity("D")
			time.Sleep(time.Second)
		}
	}()


	go func() {

		fmt.Println(graph.HasPath("A", "B"))
		time.Sleep(time.Second)

		fmt.Println(graph.HasPath("B", "C"))
		time.Sleep(time.Second)
	}()


	go func() {

		fmt.Println(graph.GetPathPrice("A", "B"))
		time.Sleep(time.Second)

		fmt.Println(graph.GetPathPrice("B", "C"))
		time.Sleep(time.Second)
	}()

	select {}
}
