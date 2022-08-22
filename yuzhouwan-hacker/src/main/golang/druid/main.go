package main

import (
	"fmt"
	"github.com/davecgh/go-spew/spew"
	"github.com/grafadruid/go-druid"
	"log"
	"time"

	"github.com/grafadruid/go-druid/builder/intervals"

	"github.com/grafadruid/go-druid/builder"
	"github.com/grafadruid/go-druid/builder/aggregation"
	"github.com/grafadruid/go-druid/builder/datasource"
	"github.com/grafadruid/go-druid/builder/filter"
	"github.com/grafadruid/go-druid/builder/granularity"
	"github.com/grafadruid/go-druid/builder/query"
)

// Copied from https://github.com/grafadruid/go-druid/blob/master/examples/main.go

func main() {

	// Connection
	var druidOpts []druid.ClientOption
	druidOpts = append(druidOpts, druid.WithSkipTLSVerify())
	d, err := druid.NewClient("http://localhost:8082", druidOpts...)
	if err != nil {
		log.Fatal(err)
	}

	// Version
	status, _, err := d.Common().Status()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("{\"version\": \"" + status.Version + "\"}") // {"version": "0.22.0"}

	// DataSource
	t := datasource.NewTable().SetName("wikipedia")

	// Time range
	i := intervals.NewInterval()
	i.SetInterval(time.Unix(0, 0), time.Now())
	i2 := intervals.NewInterval()
	i2.SetIntervalWithString("2021-01-21T14:59:05.000Z", "P1D")
	is := intervals.NewIntervals().SetIntervals([]*intervals.Interval{i, i2})

	// Aggregation
	c := aggregation.NewCount().SetName("count")
	aa := []builder.Aggregator{c}

	// Columns
	s1 := filter.NewSelector().SetDimension("countryName").SetValue("France")
	s2 := filter.NewSelector().SetDimension("cityName").SetValue("Paris")
	n := filter.NewNot().SetField(s2)
	a := filter.NewAnd().SetFields([]builder.Filter{s1, n})
	m := granularity.NewSimple().SetGranularity(granularity.All)
	ts := query.NewTimeseries().SetDataSource(t).SetIntervals(is).SetAggregations(aa).SetGranularity(m).SetFilter(a).SetLimit(10)
	var results interface{}

	// Execute
	_, err = d.Query().Execute(ts, &results)
	if err != nil {
		log.Fatalf("Execute failed, %s", err)
	}

	// Result
	/**
	([]interface {}) (len=1 cap=1) {
	 (map[string]interface {}) (len=2) {
	  (string) (len=9) "timestamp": (string) (len=24) "2016-06-27T00:00:11.080Z",
	  (string) (len=6) "result": (map[string]interface {}) (len=1) {
	   (string) (len=5) "count": (float64) 79
	  }
	 }
	}
	*/
	spew.Dump(results)
}
